/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.entity.Device;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import com.scosyf.mqtt.integration.common.message.sys.SysMessage;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.SysMsgTypeEnum;
import com.scosyf.mqtt.integration.dao.DeviceDao;
import com.scosyf.mqtt.integration.dao.DeviceOnlineDao;
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

    @Autowired
    private DeviceOnlineDao deviceOnlineDao;

    /**
     * 记录设备上下线
     *
     * TODO 如果设备的WIFI模块换了，即mac变了，需要在后台对这台设备进行更换mac，让deviceId和mac重新配对
     *
     **/
    public Message<String> handleOnOff(SysMessage sysMessage) {
        try {
            // 消息体不存在 / 非上下线消息
            if (sysMessage == null || !sysMessage.getSysMsgTypeEnum().equals(SysMsgTypeEnum.ONOFF)) {
                return null;
            }
            OnlineMessage onlineMessage = (OnlineMessage) sysMessage;
            // d:rinnai:SR:01:SR:98D863BE7116
            String[] identifies = onlineMessage.getUserName().split(MqttConstant.USERNAME_SPLITTER);
            String mac = identifies[identifies.length - 1];
            // 只处理设备的上下线，App的不处理
            if (MqttConstant.MAC_LENGTH != mac.length()) {
                return null;
            }
            Device device = deviceDao.getByMac(mac);
            if (device == null) {
                return null;
            }
            // 保存设备每次上下线消息
            deviceOnlineDao.saveOnlineRecord(onlineMessage, device);
            // 设备最近状态
            JSONObject deviceLastStatPayload = null;
            if (onlineMessage.getOnline()) {
                String[] topicItems = onlineMessage.getTopicItems();
                String node = topicItems[topicItems.length - 4];
                LOGGER.info(">>> 设备上线, mac:{}, clientId:{}, node:{}, ip:{}",
                        mac, onlineMessage.getClientId(), node, onlineMessage.getIpAddress());

                // 保存mac，clientId等信息，作为上线标识
                deviceDao.saveOnline(onlineMessage.getClientId(), mac, onlineMessage.getIpAddress(), node);
                deviceLastStatPayload = new JSONObject();
                deviceLastStatPayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_ON);
            } else {
                LOGGER.info(">>> 设备离线, mac:{}, clientId:{}, reason:{}",
                        mac, onlineMessage.getClientId(), onlineMessage.getReason());
                // 通过mac和clientId 判断上次的上线情况（下线失败的处理）
                if (deviceDao.saveOffline(onlineMessage.getClientId(), mac)) {
                    deviceLastStatPayload = new JSONObject();
                    deviceLastStatPayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_OFF);
                }
            }
            if (Objects.isNull(deviceLastStatPayload)) {
                return null;
            }
            // 设置服务端保留消息topic：kunbu/retain/
            String topic = MqttConstant.DEFAULT_TOPIC_PERFIX + MqttConstant.TOPIC_SPLITTER
                    + MqttConstant.RETAIN_TOPIC_SUFFIX + MqttConstant.TOPIC_SPLITTER;
            // 组装message：上下线处理完成后，设备最近状态作为保留消息，供App使用
            Message<String> pubMsg = MessageBuilder
                    .withPayload(deviceLastStatPayload.toJSONString())
                    .setHeader(MqttHeaders.TOPIC, topic)
                    .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                    .build();
            return pubMsg;
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, sysMessage:{}", sysMessage, e);
            return null;
        }
    }
}
