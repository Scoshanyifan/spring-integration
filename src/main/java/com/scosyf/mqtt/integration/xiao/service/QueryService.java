package com.scosyf.mqtt.integration.xiao.service;

import com.scosyf.mqtt.integration.xiao.common.message.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("queryService")
public class QueryService {

    private static final Logger log = LoggerFactory.getLogger(QueryService.class);

    public Message<String> handleQuery(RawMessage jMessage) {
        return null;
    }

}
