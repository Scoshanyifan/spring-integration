package com.scosyf.mqtt.integration.service;

import com.scosyf.mqtt.integration.entity.ImageBizMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-11 09:29
 **/
@Component("imageService")
public class ImageMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageMessageService.class);

    public void handleImageMessage(Message<ImageBizMessage> message) {

        LOGGER.info(">>> 处理image，message：{}", message);

    }


}
