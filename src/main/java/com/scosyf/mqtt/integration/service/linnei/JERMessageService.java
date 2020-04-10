package com.scosyf.mqtt.integration.service.linnei;

import com.scosyf.mqtt.integration.common.message.linnei.JERMessage;
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

        LOGGER.info(">>> 记录error，message：{}", message);
    }


}
