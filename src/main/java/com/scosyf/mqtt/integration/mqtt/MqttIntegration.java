package com.scosyf.mqtt.integration.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.config.MqttConfig;
import com.scosyf.mqtt.integration.constant.Constant;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;
import com.scosyf.mqtt.integration.entity.BizMessage;
import com.scosyf.mqtt.integration.utils.ExecutorFactoryUtil;
import com.scosyf.mqtt.integration.utils.MessageTransferUtil;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

import java.util.Objects;

/**
 * https://docs.spring.io/spring-integration/reference/html/mqtt.html#mqtt
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 10:21
 **/
@Configuration
public class MqttIntegration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttIntegration.class);

    @Autowired
    private MqttConfig mqttConfig;

    /**
     * 用于生产 mqtt 客户端
     *
     **/
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        LOGGER.info(">>> mqttConfig:{}", JSONObject.toJSONString(mqttConfig));

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{mqttConfig.getUrl()});
        options.setUserName(mqttConfig.getUserName());
        options.setPassword(mqttConfig.getPassword().toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 系统消息订阅
     *
     **/
    @Bean
    public MessageProducerSupport sysMsgInBound() {

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttConfig.getSysClientId(), mqttClientFactory(), mqttConfig.getSysTopic());

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(new DirectChannel());
        adapter.setCompletionTimeout(Constant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setQos(Constant.DEFAULT_QOS);
        return adapter;
    }

    /**
     * 业务消息订阅
     *
     **/
    @Bean
    public MessageProducerSupport bizMsgInBound() {

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttConfig.getBizClientId(), mqttClientFactory(), mqttConfig.getBizTopic());

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(new DirectChannel());
        adapter.setCompletionTimeout(Constant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setQos(Constant.DEFAULT_QOS);
        return adapter;
    }

    /**
     * 消息送出
     *
     **/
    @Bean
    public MessageHandler msgOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(mqttConfig.getPublisherId(), mqttClientFactory());
        messageHandler.setAsync(true);
        return messageHandler;
    }


    /** ======================================== 消息处理 ======================================== */

    /**
     * 系统消息处理
     *
     **/
    @Bean
    public IntegrationFlow sysMsgFlow() {
        return IntegrationFlows.from(sysMsgInBound())
                .channel(c -> c.executor(ExecutorFactoryUtil.buildSysExecutor()))
                .handle(MessageTransferUtil::mqttMessage2SysMessage)
//                .filter(SysMessage.class, MessageTransformer::sysOnlineFilter)
                .handle("sysMsgService", "onOffline")
                .filter(Objects::nonNull)
                .handle(msgOutbound())
                .get();
    }

    /**
     * 业务消息处理
     *
     **/
    @Bean
    public IntegrationFlow bizMsgFlow() {
        return IntegrationFlows
                // 消息来源
                .from(bizMsgInBound())
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
