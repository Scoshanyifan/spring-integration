package com.scosyf.mqtt.integration.service;

import com.scosyf.mqtt.integration.common.message.J00Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-11 09:29
 **/
@Component("j00Service")
public class J00MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(J00MessageService.class);

    public void handleHeatOven(Message<J00Message> message) {

        LOGGER.info(">>> 处理book，message：{}", message.getPayload());
    }

    public void handleHotWater(Message<J00Message> message) {

        LOGGER.info(">>> 处理book，message：{}", message.getPayload());
    }
}
