/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.message.SysMessage;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;
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
        JSONObject payloadJson = JSONObject.parseObject(sysMessage.getPayload());
        // $SYS/brokers/+/clients/+/connected
        String[] topicItems = sysMessage.getTopicItems();
        String state = topicItems[topicItems.length - 1];
        String userName = payloadJson.getString(MqttConstant.PAYLOAD_USER_NAME);
        String clientId = payloadJson.getString(MqttConstant.PAYLOAD_CLIENT_ID);
        // app or device
        String[] identifies = userName.split(MqttConstant.NAME_SPLITTER);
        String id = identifies[identifies.length - 1];
        if (MqttConstant.MAC_LENGTH != id.length()) {
            LOGGER.info("非wifi设备:{}", id);
            return null;
        }
        JSONObject mqttPayload = null;
        mqttPayload.put(MqttConstant.PAYLOAD_MSG_TYPE, MsgTypeEnum.BCK.name());
        switch (state) {
            case MqttConstant.CONNECTED:
                String mac = topicItems[topicItems.length - 2];
                String ip = payloadJson.getString(MqttConstant.PAYLOAD_ONLINE_IP);
                LOGGER.info(">>> 设备已上线, mac:{}", mac);

                // todo save db
                mqttPayload = new JSONObject();
                mqttPayload.put(MqttConstant.PAYLOAD_ONOFF, MqttConstant.PAYLOAD_ONOFF_ON);
                break;
            case MqttConstant.DISCONNECTED:
                LOGGER.info(">>> 设备尝试离线, name:{}", userName);

                // todo save db
                mqttPayload = new JSONObject();
                mqttPayload.put(MqttConstant.PAYLOAD_ONOFF, MqttConstant.PAYLOAD_ONOFF_OFF);
                break;
            default:
                LOGGER.warn("非上下线消息，请检查前面的过滤是否有问题");
                mqttPayload = null;
        }
        if (Objects.isNull(mqttPayload)) {
            return null;
        }
        String topic = MqttConstant.DEFAULT_TOPIC_PERFIX + id + MqttConstant.TOPIC_SPLITTER
                + MsgTypeEnum.BCK.name().toLowerCase() + MqttConstant.TOPIC_SPLITTER;
        // 组装响应message
        Message<String> pubMsg = MessageBuilder
                .withPayload(mqttPayload.toJSONString())
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                .build();
        return pubMsg;
    }
}
