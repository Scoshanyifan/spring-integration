package com.scosyf.mqtt.integration.xiao.service;

import com.scosyf.mqtt.integration.xiao.common.message.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("nilService")
public class NilService {

    private static final Logger log = LoggerFactory.getLogger(NilService.class);

    public Message<String> handle(RawMessage jMessage) {
        return null;
    }

}
