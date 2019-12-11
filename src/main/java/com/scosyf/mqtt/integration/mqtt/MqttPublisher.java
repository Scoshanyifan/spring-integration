package com.scosyf.mqtt.integration.mqtt;

/**
 * @author: KunBu
 * @time: 2019/12/11 16:13
 * @description:
 */
public interface MqttPublisher {

    void send(String topic, String out);

}
