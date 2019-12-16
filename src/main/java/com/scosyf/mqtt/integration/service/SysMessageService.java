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
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.MsgTopicEnum;
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
        // scosyf/sys/+/client/+/connected
        String[] tokens = sysMessage.getTopicTokens();
        String state = tokens[tokens.length - 1];
        String userName = jsonMsg.getString(MqttConstant.USER_NAME);
        String clientId = jsonMsg.getString(MqttConstant.CLIENT_ID);
        String[] unTokens = userName.split(MqttConstant.NAME_SPLITTER);
        String id = unTokens[unTokens.length - 1];
        if (MqttConstant.MAC_LENGTH != id.length()) {
            LOGGER.info("非wifi设备:{}", id);
            return null;
        }
        JSONObject mqttPayload = new JSONObject();
        mqttPayload.put(MqttConstant.PTN, MsgTopicEnum.SYS.name());
        switch (state) {
            case MqttConstant.CONNECTED:
                String node = tokens[MqttConstant.CLIENTS_INDEX - 1];
                String ip = jsonMsg.getString(MqttConstant.ONLINE_IP);
                LOGGER.info(">>> 设备已上线, name:{}", userName);

                mqttPayload.put(MqttConstant.ONLINE, MqttConstant.ON);
                break;
            case MqttConstant.DISCONNECTED:
                LOGGER.info(">>> 设备尝试离线, name:{}", userName);
                if (true) {
                    mqttPayload.put(MqttConstant.ONLINE, MqttConstant.OFF);

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
        String topic = MqttConstant.DEFAULT_TOPIC_PERFIX + id + MqttConstant.TOPIC_SPLITTER
                + MsgTopicEnum.SYS.name().toLowerCase() + MqttConstant.TOPIC_SPLITTER;
        Message<String> pubMsg = MessageBuilder.withPayload(mqttPayload.toJSONString())
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                .build();
        return pubMsg;
    }
}
