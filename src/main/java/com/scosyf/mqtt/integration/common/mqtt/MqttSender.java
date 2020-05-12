package com.scosyf.mqtt.integration.common.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-05-12 11:06
 **/
@Component
public class MqttSender {

    private static final Logger logger = LoggerFactory.getLogger(MqttSender.class);

    @Autowired
    @Qualifier("messageHandler")
    private MessageHandler messageHandler;

    public void send(String topic, Object payload) {
        Message<Object> pubMsg = MessageBuilder
                .withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, topic)
                .build();
        logger.info("send message, topic:{}, payload:{}", topic, payload);
        messageHandler.handleMessage(pubMsg);
    }

}
