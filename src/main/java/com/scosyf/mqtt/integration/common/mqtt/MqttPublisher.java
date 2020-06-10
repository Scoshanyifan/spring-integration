package com.scosyf.mqtt.integration.common.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author: KunBu
 * @time: 2019/12/19 9:37
 * @description:
 */
//@Component
@MessagingGateway(name = "mqttPublisher", defaultRequestChannel = "mqttOutboundChannel")
public interface MqttPublisher {

    /**
     * 消息内容out，可以为byte[]或String
     *
     * 传其他类型会报错，因为Mqtt消息协议最终使用字节传输
     * This default converter can only handle 'byte[]' or 'String' payloads; consider adding a transformer to your flow definition,
     * or provide a BytesMessageMapper, or subclass this converter for java.util.ArrayList payloads
     *
     **/
    void send(@Header(MqttHeaders.TOPIC) String topic, Object out);
}
