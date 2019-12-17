package com.scosyf.mqtt.integration.service;

import com.scosyf.mqtt.integration.common.message.BookBizMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-11 09:29
 **/
@Component("bookService")
public class BookMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookMessageService.class);

    public void handleBookMessage(Message<BookBizMessage> message) {

        LOGGER.info(">>> 处理book，message：{}", message.getPayload());

    }


}
