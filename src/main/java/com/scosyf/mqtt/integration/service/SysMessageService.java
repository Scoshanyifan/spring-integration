/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;
import com.scosyf.mqtt.integration.entity.BizMessage;
import com.scosyf.mqtt.integration.entity.SysMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zhaowz@hadlinks.com
 *         Function:
 */
@Component("sysMsgService")
public class SysMessageService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SysMessageService.class);

    public Message<String> onOffline(SysMessage sysMessage) {
        LOGGER.info(">>> onOffline, sysMessage:{}", sysMessage);

        JSONObject jsonMsg = JSON.parseObject(sysMessage.getMessage());
        String[] tokens = sysMessage.getTopicTokens();
        String state = tokens[tokens.length - 1];
        String userName = jsonMsg.getString(SysMessage.ONLINE_USERNAME);
        String clientId = jsonMsg.getString(SysMessage.ONLINE_CLIENTID);
        String[] unTokens = userName.split(BizMessage.USERNAME_SPLIT_TOKEN);
        String id = unTokens[unTokens.length - 1];
        if (BizMessage.MAC_LEN != id.length()) {
            LOGGER.info("非wifi设备:{}", id);
            return null;
        }
        JSONObject mqttPayload = new JSONObject();
        mqttPayload.put(BizMessage.PTN, MsgTypeEnum.SYS.name());
        switch (state) {
            case SysMessage.CONNECTED:
                String node = tokens[SysMessage.CLIENTS_INDEX - 1];
                String ip = jsonMsg.getString(SysMessage.ONLINE_IP);
                LOGGER.info(">>> 设备已上线, name:{}", userName);

                mqttPayload.put(BizMessage.ONLINE, BizMessage.ONLINE_ON);
                break;
            case SysMessage.DISCONNECTED:
                LOGGER.info(">>> 设备尝试离线, name:{}", userName);
                if (true) {
                    mqttPayload.put(BizMessage.ONLINE, BizMessage.ONLINE_OFF);

                } else {
                    mqttPayload = null;
                }
                break;
            default:
                LOGGER.warn("非上下线消息，请检查前面的过滤是否有问题");
                mqttPayload = null;
        }
        if (Objects.isNull(mqttPayload)) {
            return null;
        }
        String topic = BizMessage.DEFAULT_TOPIC_PERFIX + id + BizMessage.TOPIC_SPLIT_TOKEN
                + MsgTypeEnum.SYS.name().toLowerCase() + BizMessage.TOPIC_SPLIT_TOKEN;
        Message<String> pubMsg = MessageBuilder.withPayload(mqttPayload.toJSONString())
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                .build();
        return pubMsg;
    }
}
