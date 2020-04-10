package com.scosyf.mqtt.integration.common.mqtt;

import com.scosyf.mqtt.integration.xiao.common.message.RawMessage;
import com.scosyf.mqtt.integration.common.config.MqttYmlConfig;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.common.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.common.util.ExecutorFactoryUtil;
import com.scosyf.mqtt.integration.common.util.MessageTransferUtil;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Objects;

/**
 * 集成spring-integration的mqtt消息处理器
 *
 * 组件介绍：
 * 1. 消息体：A generic message representation with headers and body.
 *    @see org.springframework.messaging.Message
 * 2. 消息通道：Defines methods for sending messages.
 *    @see org.springframework.messaging.MessageChannel
 * 3. 通道适配器：用于和外部数据交换，连接应用和消息系统
 *    @see org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
 * 4. 消息处理：
 *    @see org.springframework.messaging.MessageHandler
 * 5. 服务激活：
 *    @see org.springframework.integration.annotation.ServiceActivator
 * 6. 消息网关：用于和外部数据的传输
 *    @see org.springframework.integration.annotation.MessagingGateway
 *
 *
 * mqtt:
 * https://docs.spring.io/spring-integration/reference/html/mqtt.html
 * https://docs.spring.io/spring-integration/docs/current/reference/html/dsl.html#java-dsl
 *
 * demo：
 * https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference
 * https://github.com/spring-projects/spring-integration-samples/blob/master/basic/mqtt/src/main/java/org/springframework/integration/samples/mqtt/Application.java
 *
 * 参考：
 * https://segmentfault.com/a/1190000017811919?utm_source=tag-newest
 * https://segmentfault.com/a/1190000015601875?utm_source=tag-newest
 * https://www.cnblogs.com/oldwangneverdie/p/9015880.html
 * https://my.oschina.net/NeedLoser/blog/803404
 *
 * @author kunbu
 * @date 2019/12/16
 **/
@Configuration
public class MqttSpringIntegration {

    private static final Logger logger = LoggerFactory.getLogger(MqttSpringIntegration.class);

    @Autowired
    private MqttYmlConfig mqttYmlConfig;

    /** ------------------------------- 配置及工厂 ----------------------------- */

    @Bean
    public MqttConnectOptions mqttConnectOptions() {

        MqttConnectOptions options = new MqttConnectOptions();
        //基础信息
        options.setServerURIs(new String[]{mqttYmlConfig.getHost()});
        options.setUserName(mqttYmlConfig.getUserName());
        options.setPassword(mqttYmlConfig.getPassword().toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(MqttConstant.DEFAULT_CONNECTION_TIMEOUT);
        // 设置心跳：1.5*20秒
        options.setKeepAliveInterval(20);
        // 设置最大并发数
        options.setMaxInflight(10);
        // 设置自动重连
        options.setAutomaticReconnect(false);
        // 设置是否清除session（如果不清除，clientId不变的情况下会保存身份信息和离线消息）
        // TODO 设置后重启消息应用，离线消息会正常处理（但是存在报错现象，提示subscriber找不到）
        options.setCleanSession(false);

        // 设置遗嘱
//        options.setWill();
        // 设置ssl
//        options.setSSLProperties();

        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    /** ------------------------------- 入站 -----------------------------
     *
     *  1.Broker推送消息到订阅了相应topic的client中，即2个MqttPahoMessageDrivenChannelAdapter生成的client
     *  2.待client拿到消息后，放到channel中，即MessageChannel
     *  3.最后IntegrationFlow从指定消息源中取消息，再进行处理
     *
     **/


    /**
     * MqttPahoMessageDrivenChannelAdapter作为消息起点，经历以下操作：
     * 1. 首先是配置订阅信息，客户端工厂等等
     * 2. 作为Bean后，调用doStart()方法进行connectAndSubscribe()，生成客户端并监听回调
     * 3. adapter实现MqttCallback，重写了connectionLost(cause)，断开后会进行重连scheduleReconnect()
     * 4. 同时adapter也重写了messageArrived(topicName, aMessage)，会把接收到的message发送出去sendMessage(message)
     * 5. 其实就是塞进管道中：this.messagingTemplate.send(getOutputChannel(), message)
     *
     **/
    @Bean
    public MessageProducerSupport sysInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttYmlConfig.getSysClientId(),
                mqttClientFactory(),
                mqttYmlConfig.getSysTopic()
        );

        /**
         * TODO 如果有多个inbound，不能指向同一个消息通道，否则流from时会拿到各种来源的消息
         *
         * 指定生成的消息应该发送到哪个通道，如果不手动设置入站消息通道，系统也会自动创建
         **/
//        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setCompletionTimeout(MqttConstant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(MqttConstant.QOS_DEFAULT);
        return adapter;
    }

    @Bean
    public MessageProducerSupport bizInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttYmlConfig.getBizClientId(),
                mqttClientFactory(),
                mqttYmlConfig.getBizTopic()
        );
        adapter.setCompletionTimeout(MqttConstant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(MqttConstant.QOS_DEFAULT);
        return adapter;
    }

    /**
     * ------------------------------- 出站 -----------------------------
     *
     *  1.消息应用处理服务器（client）通过MessageGateway把消息放到指定的MessageChannel（mqttOutboundChannel）
     *  2.出站用的MessageHandler"监听"着相应的channel（@ServiceActivator）
     *  3.当发现有消息时，就会推到broker中（topic在gateway中指定，也可以配置默认topic）
     *
     **/

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MessageHandler和MessageProducer：
     * 两者都是消息处理组件，区别是前者Contract for handling a Message，而后者是sending messages to a MessageChannel
     * 即一个可以理解为普通站台（用于后面的消息出站），另一个中央大站（消息入站）
     *
     **/
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttYmlConfig.getClientId(),
                mqttClientFactory()
        );

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(MqttConstant.QOS_DEFAULT);
//        messageHandler.setDefaultTopic("defaultTopic");
        return messageHandler;
    }

    /** ============================ 业务处理（系统和业务） ============================== */

    /**
     * Spring Integration Java DSL ：
     * IntegrationFlows和IntegrationFlowBuilder来实现使用Fluent API来定义流程
     *
     **/
    @Bean
    public IntegrationFlow sysMsgFlow() {
        return IntegrationFlows
                /**
                 * 消息源是MessageProducer，如果发现没有绑定通道，会新建setOutputChannel(outputChannel)
                 * 生成IntegrationFlowBuilder，类似流的执行，分阶段处理
                 **/
                .from(sysInbound())
                /**
                 * 转换通道（上游的DirectChannel -> ExecutorChannel）
                 * 其中加入了负载均衡策略 LoadBalancingStrategy
                 **/
                .channel(channels -> channels.executor(ExecutorFactoryUtil.buildSysExecutor()))
                /**
                 * 消息处理节点endPoint：用于转成业务模型。
                 * 当前重载的参数是GenericHandler，为函数式接口，需用户重写：Object handle(P payload, MessageHeaders headers);
                 *
                 * 处理后的业务数据以怎样的形式在流中流通：GenericMessage（具体可参考adapter接收消息之后的流程）
                 **/
                .handle(MessageTransferUtil::mqttMessage2SysMessage)
                /**
                 * 消息处理节点endPoint：对上下线消息进行记录处理（如果以后有其他需求，可以选择route）
                 **/
                .handle("sysMsgService", "handleOnOff")
                /**
                 * 消息处理节点endPoint：判断消息是否为空
                 **/
                .filter(Objects::nonNull)
                /**
                 * 消息处理节点endPoint：如果有消息返回输出
                 **/
                .handle(mqttOutbound())
                /**
                 * 获取集成流程并注册为Bean
                 **/
                .get();
    }

    @Bean
    public IntegrationFlow bizMsgFlow() {
        return IntegrationFlows
                .from(bizInbound())
                .channel(channels -> channels.executor(ExecutorFactoryUtil.buildBizExecutor()))
                /** 处理Json消息 */
//                .handle(MessageTransferUtil::mqttMessage2BizMessage)
//                /**
//                 * 消息节点：通过route路由来分流，依据msgType，走不同的消息处理（subFlow子流）
//                 **/
//                .<BizMessage, LinBizTypeEnum>route(
//                        BizMessage::getBizMsgTypeEnum,
//                        routerSpec -> routerSpec
//                                .subFlowMapping(LinBizTypeEnum.J00, flow -> flow
//                                        .handle("j00Service", "handleDeviceInfo")
//                                        .filter(Objects::nonNull)
//                                        .handle(mqttOutbound()))
//                                .subFlowMapping(LinBizTypeEnum.J05, flow -> flow
//                                        .handle("j00Service", "handleDeviceInfo")
//                                        .filter(Objects::nonNull)
//                                        .handle(mqttOutbound()))
//                                .subFlowMapping(LinBizTypeEnum.JER, flow -> flow
//                                        .handle( "jerService", "handleError")
//                                        .filter(Objects::nonNull)
//                                        .handle(mqttOutbound()))
//                                // TODO channelMapping是什么作用
////                                .channelMapping(LinBizTypeEnum.JER, "channel")
//                                .defaultOutputToParentFlow()
//                )
//                .get();
                /** 处理原生消息 */
                .handle(MessageTransferUtil::mqttMessage2RawMessage)
                .<RawMessage, TopicTypeEnum>route(
                        RawMessage::getTopicTypeEnum,
                        mapping -> mapping
                                .subFlowMapping(TopicTypeEnum.up, sf -> sf
                                        .handle("upService", "handleUp")
                                        .filter(Objects::nonNull)
                                        .handle(mqttOutbound()))
                                .subFlowMapping(TopicTypeEnum.query, sf -> sf
                                        .handle("queryService", "handleQuery")
                                        .filter(Objects::nonNull)
                                        .handle(mqttOutbound()))
                                .defaultOutputToParentFlow()
                )
                .get();
    }

}
