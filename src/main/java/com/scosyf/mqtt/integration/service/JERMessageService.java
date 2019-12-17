package com.scosyf.mqtt.integration.service;

import com.scosyf.mqtt.integration.common.message.J02Message;
import com.scosyf.mqtt.integration.common.message.JERMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-11 09:29
 **/
@Component("jerService")
public class JERMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JERMessageService.class);

    public void handleError(Message<JERMessage> message) {

        LOGGER.info(">>> 处理image，message：{}", message);

    }


}
