package com.scosyf.mqtt.integration.paho;

/**
 * @author: KunBu
 * @time: 2019/12/11 16:13
 * @description:
 */
public interface IMqttPublisher {

    void push(String topic, String msg);

    void push(int qos, boolean retained, String topic, String msg);

}