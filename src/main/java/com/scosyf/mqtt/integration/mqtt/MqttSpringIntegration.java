package com.scosyf.mqtt.integration.mqtt;

import com.scosyf.mqtt.integration.config.MqttYmlConfig;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;
import com.scosyf.mqtt.integration.entity.BizMessage;
import com.scosyf.mqtt.integration.utils.ExecutorFactoryUtil;
import com.scosyf.mqtt.integration.utils.MessageTransferUtil;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
 * mqtt:
 * https://docs.spring.io/spring-integration/reference/html/mqtt.html
 *
 * demo：
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

    /** ------------------------------- 发布者 ----------------------------- */

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttYmlConfig.getClientId(),
                mqttClientFactory()
        );

        messageHandler.setAsync(true);
        return messageHandler;
    }

    /** ------------------------------- 订阅者 ----------------------------- */

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducerSupport sysInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttYmlConfig.getSysClientId(),
                mqttClientFactory(),
                mqttYmlConfig.getSysTopic()
        );
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannel());
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

        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setCompletionTimeout(MqttConstant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(MqttConstant.QOS_DEFAULT);
        return adapter;
    }

    /** ============================ 业务处理 ============================== */

    /**
     * 系统消息处理 consumer
     *
     **/
    @Bean
    public IntegrationFlow sysMsgFlow() {
        return IntegrationFlows.from(sysInbound())
                .channel(c -> c.executor(ExecutorFactoryUtil.buildSysExecutor()))
                .handle(MessageTransferUtil::mqttMessage2SysMessage)
//                .filter(SysMessage.class, MessageTransformer::sysOnlineFilter)
                .handle("sysMsgService", "onOffline")
                .filter(Objects::nonNull)
                .handle(mqttOutbound())
                .get();
    }

    /**
     * 业务消息处理 consumer
     *
     **/
    @Bean
    public IntegrationFlow bizMsgFlow() {
        return IntegrationFlows
                // 消息来源
                .from(bizInbound())
                // 分配通道
                .channel(c -> c.executor(ExecutorFactoryUtil.buildBizExecutor()))
                // 消息转换成业务使用
                .handle(MessageTransferUtil::mqttMessage2BizMessage)
                // 通过route来选择路由，按照msgType分配，走不同的消息处理
                .<BizMessage, MsgTypeEnum>route(BizMessage::getMsgTypeEnum,
                        mapping -> mapping
                                .subFlowMapping(MsgTypeEnum.BOOK, bookIntegrationFlow())
                                .subFlowMapping(MsgTypeEnum.IMAGE, imageIntegrationFlow())
                )
                // 获得IntegrationFlow实体，配置为Spring的Bean
                .get();
    }

    private IntegrationFlow bookIntegrationFlow() {

        return flow -> flow.handle("bookService", "handleBookMessage");
    }

    private IntegrationFlow imageIntegrationFlow() {

        return flow -> flow.handle("imageService", "handleImageMessage");
    }

}
