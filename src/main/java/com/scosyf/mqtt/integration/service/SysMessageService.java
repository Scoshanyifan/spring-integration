/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import com.scosyf.mqtt.integration.common.message.sys.SysMessage;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.SysMsgTypeEnum;
import com.scosyf.mqtt.integration.dao.DeviceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DeviceDao deviceDao;

    /**
     * 记录设备上下线
     *
     **/
    public Message<String> handleOnOff(SysMessage sysMessage) {
        if (!sysMessage.getSysMsgTypeEnum().equals(SysMsgTypeEnum.ONOFF)) {
            return null;
        }
        OnlineMessage onlineMessage = (OnlineMessage) sysMessage;
        // d:rinnai:SR:01:SR:98D863BE7116
        String[] identifies = onlineMessage.getUserName().split(MqttConstant.USERNAME_SPLITTER);
        String mac = identifies[identifies.length - 1];
        // 只记录设备的上下限，app的不记录
        if (MqttConstant.MAC_LENGTH != mac.length()) {
            return null;
        }
        // 上下线处理完成后，设备最近状态作为保留消息，供App使用
        JSONObject mqttPayload = null;
        // $SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE7116044B/disconnected
        String[] topicItems = onlineMessage.getTopicItems();
        String onOff = topicItems[topicItems.length - 1];
        switch (onOff) {
            case SysMessage.CONNECTED:
                String node = topicItems[topicItems.length - 4];
                LOGGER.info(">>> 设备上线, mac:{}, clientId:{}, node:{}, ip:{}",
                        mac, onlineMessage.getClientId(), node, onlineMessage.getIpAddress());

                // 保存mac，clientId等信息，作为上线标识
                deviceDao.saveOnline(onlineMessage.getClientId(), mac, onlineMessage.getIpAddress(), node);
                mqttPayload = new JSONObject();
                mqttPayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_ON);
                break;
            case SysMessage.DISCONNECTED:
                LOGGER.info(">>> 设备离线, mac:{}, clientId:{}, reason:{}", mac, onlineMessage.getClientId(), onlineMessage.getReason());
                // 通过mac和clientId 判断上次的上线情况（下线失败的处理）
                if (deviceDao.saveOffline(onlineMessage.getClientId(), mac)) {
                    mqttPayload = new JSONObject();
                    mqttPayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_OFF);
                }
                break;
            default:
                //
        }
        if (Objects.isNull(mqttPayload)) {
            return null;
        }
        // 设置服务端保留消息topic：kunbu/retain/
        String topic = MqttConstant.DEFAULT_TOPIC_PERFIX + MqttConstant.TOPIC_SPLITTER
                + MqttConstant.RETAIN_TOPIC_SUFFIX + MqttConstant.TOPIC_SPLITTER;
        // 组装message
        Message<String> pubMsg = MessageBuilder
                .withPayload(mqttPayload.toJSONString())
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                .build();
        return pubMsg;
    }
}
