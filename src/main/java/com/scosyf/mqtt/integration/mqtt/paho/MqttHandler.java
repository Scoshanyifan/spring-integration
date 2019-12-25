package com.scosyf.mqtt.integration.mqtt.paho;

import com.scosyf.mqtt.integration.config.MqttPropertyConfig;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 原生mqtt消息处理
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-11 16:19
 **/
public class MqttHandler implements IMqttPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttHandler.class);

    private MqttClient mqttClient;

    @Override
    public void push(String topic, String msg) {
        push(MqttConstant.QOS_DEFAULT, false, topic, msg);
    }

    @Override
    public void push(int qos, boolean retained, String topic, String msg) {
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(qos);
            message.setRetained(retained);
            message.setPayload(msg.getBytes(MqttConstant.CHARSET_DEFAULT));
            mqttClient.publish(topic, message);
        } catch (Exception e) {
            LOGGER.error(">>> IMqttHandler push error", e);
        }
    }

    /** 如果不适用spring-integration 则使用单例模式 */

    private static class Holder {
        private static final MqttHandler INSTANCE = new MqttHandler();
    }

    private MqttHandler() {
        connect();
    }

    public static MqttHandler getInstance() {
        return Holder.INSTANCE;
    }

    private void connect() {
        MqttPropertyConfig.printProperty();
        try {
            mqttClient = new MqttClient(
                    MqttPropertyConfig.getHost(),
                    MqttPropertyConfig.getClientId(),
                    // MqttClientPersistence以内存保存
                    new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(MqttPropertyConfig.getUserName());
            options.setPassword(MqttPropertyConfig.getPassword().toCharArray());

            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(false);
            // 设置是否自动重连
            options.setAutomaticReconnect(false);
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);
            // 最大并发数
            options.setMaxInflight(10);
            // 设置ssl，will等

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    LOGGER.error(">>> IMqttHandler 连接丢失", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    LOGGER.info(">>> IMqttHandler 消息topic:{}，payload:{}", topic, new String(message.getPayload(), "utf8"));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    LOGGER.info(">>> IMqttHandler 消息传递：{}", token.isComplete());
                }
            });
            mqttClient.connect(options);
        } catch (Exception e) {
            LOGGER.error(">>> IMqttHandler 消息应用连接异常", e);
        }
    }

}
