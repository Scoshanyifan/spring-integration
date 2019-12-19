package com.scosyf.mqtt.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 组件介绍：
 *
 * 1. 消息体：A generic message representation with headers and body.
 * @see org.springframework.messaging.Message
 *
 * 2. 消息通道：Defines methods for sending messages.
 * @see org.springframework.messaging.MessageChannel
 *
 * 3. 通道适配器：用于和外部数据交换，连接应用和消息系统
 * @see org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
 *
 * 4. 消息处理：
 * @see org.springframework.messaging.MessageHandler
 *
 * 5. 服务激活：
 * @see org.springframework.integration.annotation.ServiceActivator
 *
 * 6. 消息网关：用于和外部数据的传输
 * @see org.springframework.integration.annotation.MessagingGateway
 *
 **/
@SpringBootApplication
public class SpringIntegrationApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringIntegrationApplication.class);

    public static void main(String[] args) {

        LOGGER.info("\n========================================================="
                + "\n                                                         "
                + "\n          Welcome to Spring Integration!                 "
                + "\n                                                         "
                + "\n    For more information please visit:                   "
                + "\n    https://spring.io/projects/spring-integration        "
                + "\n                                                         "
                + "\n=========================================================");

        LOGGER.info("\n========================================================="
                + "\n                                                         "
                + "\n    这是一个基于MQTT订阅的消息处理服务 -                     "
                + "\n    订阅关键的TOPIC，将收到的mqtt消息处理并最终写入数据        "
                + "\n=========================================================");

        SpringApplication.run(SpringIntegrationApplication.class, args);


    }

}
