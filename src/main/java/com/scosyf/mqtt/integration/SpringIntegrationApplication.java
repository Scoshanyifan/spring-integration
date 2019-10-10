package com.scosyf.mqtt.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringIntegrationApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringIntegrationApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(SpringIntegrationApplication.class, args);


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
    }

}
