package com.scosyf.mqtt.integration.mqtt;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.scosyf.mqtt.integration.config.MqttConfig;
import com.scosyf.mqtt.integration.constant.Constant;
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
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        MqttConfig.Server server = mqttConfig.getServer();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{server.getUrl()});
        options.setUserName(server.getUserName());
        options.setPassword(server.getPassword().toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 业务消息订阅
     *
     **/
    @Bean
    public MessageProducerSupport bizMsgInBound() {
        MqttConfig.BizConsumer bizConsumer = mqttConfig.getBizConsumer();

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(bizConsumer.getBizClientId(), mqttClientFactory(), bizConsumer.getBizTopic());

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(new DirectChannel());
        adapter.setCompletionTimeout(Constant.DEFAULT_COMPLETION_TIMEOUT);
        adapter.setQos(Constant.DEFAULT_QOS);
        return adapter;
    }

    /**
     * 系统消息订阅
     *
     **/
    @Bean
    public MessageProducerSupport sysMsgInBound() {
        MqttConfig.SysConsumer sysConsumer = mqttConfig.getSysConsumer();

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(sysConsumer.getSysClientId(), mqttClientFactory(), sysConsumer.getSysTopic());

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
                new MqttPahoMessageHandler(mqttConfig.getServer().getPublisherId(), mqttClientFactory());
        messageHandler.setAsync(true);
        return messageHandler;
    }

    /** ======================================== 消息处理 ======================================== */

    /**
     * 业务消息处理
     *
     **/
    @Bean
    public IntegrationFlow bizMsgFlow() {
        return IntegrationFlows.from(bizMsgInBound())
                .channel(c -> c.executor(ExecutorFactoryUtil.buildBizExecutor()))
                .handle(MessageTransferUtil::)
    }

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

}
