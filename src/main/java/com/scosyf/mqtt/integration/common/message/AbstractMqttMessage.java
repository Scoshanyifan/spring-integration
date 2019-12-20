package com.scosyf.mqtt.integration.common.message;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-20 17:52
 **/
public abstract class AbstractMqttMessage {

    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "AbstractMqttMessage{" +
                "payload='" + payload + '\'' +
                '}';
    }
}
