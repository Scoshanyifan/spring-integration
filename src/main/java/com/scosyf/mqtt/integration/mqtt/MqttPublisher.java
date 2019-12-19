package com.scosyf.mqtt.integration.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author: KunBu
 * @time: 2019/12/19 9:37
 * @description:
 */
@MessagingGateway(name = "mqttPublisher", defaultRequestChannel = "mqttOutboundChannel")
public interface MqttPublisher {

    void send(@Header(MqttHeaders.TOPIC) String topic, String out);
}
