package com.scosyf.mqtt.integration.mqtt;

import com.scosyf.mqtt.integration.config.MqttConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 原生mqtt消息处理
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-11 16:19
 **/
@Component
public class MqttHandler implements MqttPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttHandler.class);

    @Autowired
    private MqttConfig mqttConfig;

    private MqttClient mqttClient;

//    private static MqttHandler instance = new MqttHandler();

    private MqttHandler() {
        connect();
    }

    private void connect() {
        try {
            mqttClient = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttConfig.getUserName());
            options.setPassword(mqttConfig.getPassword().toCharArray());
            options.setCleanSession(mqttConfig.getCleanSession());

            options.setAutomaticReconnect(false);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);
            // ssl will ... ...

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    LOGGER.error(">>> MqttHandler 连接丢失", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    LOGGER.info(">>> MqttHandler 消息topic:{}，payload:{}", topic, new String(message.getPayload(), "utf8"));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    LOGGER.info(">>> MqttHandler 消息传递：{}", token.isComplete());
                }
            });
            mqttClient.connect(options);
        } catch (Exception e) {
            LOGGER.error(">>> MqttHandler 消息应用连接异常", e);
        }
    }

    @Override
    public void send(String topic, String out) {

    }

}
