/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.common.entity.linnei.LinDevice;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import com.scosyf.mqtt.integration.common.message.sys.SysMessage;
import com.scosyf.mqtt.integration.constant.ClientTypeEnum;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.dao.DeviceDao;
import com.scosyf.mqtt.integration.mqtt.MqttPublisher;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private MqttPublisher mqttPublisher;

    /**
     * 记录设备上下线
     *
     * TODO 如果设备的WIFI模块换了，即mac变了，需要在后台对这台设备进行更换mac，让deviceId和mac重新配对
     *
     **/
    public Message<String> handleOnOff(SysMessage sysMessage) {
        try {
            if (sysMessage == null) {
                return null;
            }
            OnlineMessage onlineMessage = (OnlineMessage) sysMessage;
            /**
             * 以下展示两种风格的消息处理
             *
             **/
            // clientid >>> DEV_{deviceType}_{sn}_{Random}
            String clientId = onlineMessage.getClientId();
            String[] clientItems = clientId.split(MqttConstant.XIAO_SPLITTER);
            String deviceType = clientItems[1];
            String sn = clientItems[2];
            // 只处理设备
            if (!clientId.startsWith(ClientTypeEnum.DEV.name())) {
                return null;
            }

            // username >>> d:rinnai:SR:01:SR:98D863BE7116
            String userName = onlineMessage.getUserName();
            String[] identifies = userName.split(MqttConstant.LINNEI_SPLITTER);
            String mac = identifies[identifies.length - 1];
            // 只处理设备
            if (MqttConstant.MAC_LENGTH != mac.length()) {
                return null;
            }

            // 保存设备每次上下线消息
            deviceDao.saveOnlineRecord(onlineMessage, mac);
            // 设备最近状态
            JSONObject deviceLastOnlinePayload = null;
            if (onlineMessage.getOnline()) {
                String[] topicItems = onlineMessage.getTopicItems();
                String node = topicItems[topicItems.length - 4];
                LOGGER.info(">>> online, mac:{}, clientId:{}, ip:{}", mac, onlineMessage.getClientId(), onlineMessage.getIpAddress());

                // 保存mac，clientId等信息，作为上线标识
                if (deviceDao.saveOnline(onlineMessage.getClientId(), mac, onlineMessage.getIpAddress(), node)) {
                    deviceLastOnlinePayload = new JSONObject();
                    deviceLastOnlinePayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_ON);
                }
            } else {
                LOGGER.info(">>> 设备离线, mac:{}, clientId:{}, reason:{}", mac, onlineMessage.getClientId(), onlineMessage.getReason());

                // 通过mac和clientId 判断上次的上线情况（下线失败的处理）
                if (deviceDao.saveOffline(onlineMessage.getClientId(), mac)) {
                    deviceLastOnlinePayload = new JSONObject();
                    deviceLastOnlinePayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_OFF);
                }
            }
            // 处理业务
            handleDownStat(sn, deviceType);
            // 返回保留消息
            return returnRetainMsg(deviceLastOnlinePayload);
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, sysMessage:{}", sysMessage, e);
            return null;
        }
    }

    /**
     * 西奥：返回多条业务消息
     *
     * @param gatewaySn
     **/
    private void handleDownStat(String gatewaySn, String deviceType) {
        // TODO 模拟sn拿到电梯信息
        List<String> liftRealNumList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(liftRealNumList)) {
            // 下发网关：/xio/{type}/{ID}/down
            String topic = MqttConstant.TOPIC_SPLITTER + deviceType + MqttConstant.TOPIC_SPLITTER
                    + gatewaySn + MqttConstant.TOPIC_SPLITTER + TopicTypeEnum.down.name();
            // TODO 模拟liftRealNumList业务数据
            List<Object> all = Lists.newArrayList();
            for (Object data : all) {
                try {
                    Thread.sleep(1000);
                    LOGGER.info(">>> handleDownStat sn:{}, topic:{}, payload:{}", gatewaySn, topic, data);
                    mqttPublisher.send(topic, data);
                } catch (InterruptedException e) {
                    LOGGER.error(">>> sleep error", e);
                }
            }
        }
    }

    /**
     * 林内：上下线处理完成后，设备最近状态作为保留消息，供App使用
     *
     * @param payload
     * @return
     **/
    private Message returnRetainMsg(JSONObject payload) {
        if (payload == null) {
            return null;
        }
        // topic：/kunbu/retain/
        String topic = MqttConstant.TOPIC_SPLITTER + MqttConstant.LINNEI_TOPIC_PERFIX + MqttConstant.TOPIC_SPLITTER
                + MqttConstant.RETAIN_TOPIC_SUFFIX + MqttConstant.TOPIC_SPLITTER;
        // 组装message：
        Message<String> pubMsg = MessageBuilder
                .withPayload(payload.toJSONString())
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                .build();
        return pubMsg;
    }
}
