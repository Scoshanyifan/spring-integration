/**
 * Project Name:rinnai-mqtt
 * File Name:SysMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.online;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.linnei.common.entity.LinDevice;
import com.scosyf.mqtt.integration.xiao.common.entity.XioDevice;
import com.scosyf.mqtt.integration.common.constant.ClientTypeEnum;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.common.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.xiao.common.constant.XioDeviceTypeEnum;
import com.scosyf.mqtt.integration.linnei.dao.LinDeviceDao;
import com.scosyf.mqtt.integration.xiao.dao.XioDiviceDao;
import com.scosyf.mqtt.integration.common.mqtt.MqttPublisher;
import com.scosyf.mqtt.integration.xiao.util.MsgUtil;
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
 *
 * @author kunbu
 */
@Component("sysMsgService")
public class SysMessageService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SysMessageService.class);

    @Autowired
    private LinDeviceDao linDeviceDao;

    @Autowired
    private XioDiviceDao xioDiviceDao;

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
            if (true) {
                return handleXio(onlineMessage);
            } else {
                return handleLin(onlineMessage);
            }
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, sysMessage:{}", sysMessage, e);
            return null;
        }
    }

    /**
     * xiao的逻辑：业务设备是电梯，电梯的状态信息由网关上报给消息应用
     **/
    private Message handleXio(OnlineMessage onlineMessage) {
        try {
            // clientid >>> DEV_{deviceType}_{sn}_{Random}
            String clientId = onlineMessage.getClientId();
            String[] clientItems = clientId.split(MqttConstant.XIAO_SPLITTER);
            String deviceType = clientItems[1];
            String sn = clientItems[2];
            // 只处理设备
            if (!clientId.startsWith(ClientTypeEnum.DEV.name())) {
                return null;
            }
            XioDevice xioDevice = xioDiviceDao.getBySn(sn);
            if (xioDevice == null) {
                return null;
            }
            // 保存设备每次上下线消息
            xioDiviceDao.saveOnlineRecord(onlineMessage, sn, xioDevice.getBizId());
            // 处理上下线
            if (onlineMessage.getOnline()) {
                LOGGER.info(">>> online, sn:{}, clientId:{}, ip:{}", sn, clientId, onlineMessage.getIpAddress());
                xioDiviceDao.online(onlineMessage.getClientId(), sn, onlineMessage.getIpAddress());

                // 网关上线后，通知其上报电梯信息
                if (XioDeviceTypeEnum.TYPE01.name().equals(deviceType)) {
                    // TODO 模拟sn拿到电梯信息
                    List<String> liftRealNumList = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(liftRealNumList)) {
                        // 通知网关的topic：/xio/{type}/{ID}/down
                        String topic = MqttConstant.TOPIC_SPLITTER + deviceType + MqttConstant.TOPIC_SPLITTER
                                + sn + MqttConstant.TOPIC_SPLITTER + TopicTypeEnum.down.name();
                        // 消息体：byte[]
                        List<Object> all = MsgUtil.downStatData(liftRealNumList);
                        for (Object data : all) {
                            try {
                                Thread.sleep(1000);
                                LOGGER.info(">>> handleDownStat sn:{}, topic:{}, payload:{}", sn, topic, data);
                                mqttPublisher.send(topic, data);
                            } catch (InterruptedException e) {
                                LOGGER.error(">>> sleep error", e);
                            }
                        }
                    }
                }
            } else {
                LOGGER.info(">>> offline, sn:{}, clientId:{}, reason:{}", sn, onlineMessage.getClientId(), onlineMessage.getReason());
                xioDiviceDao.offline(onlineMessage.getClientId(), sn);
            }
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, onlineMessage:{}", onlineMessage, e);
        }
        return null;
    }

    /**
     * linnei的逻辑：设备每次上下线，需要设置最近一次状态的保留消息，供App查看使用（非即时性）
     **/
    private Message handleLin(OnlineMessage onlineMessage) {
        try {
            // username >>> d:rinnai:SR:01:SR:98D863BE7116
            String userName = onlineMessage.getUserName();
            String[] identifies = userName.split(MqttConstant.LINNEI_SPLITTER);
            String mac = identifies[identifies.length - 1];
            // 只处理设备
            if (MqttConstant.MAC_LENGTH != mac.length()) {
                return null;
            }
            LinDevice linDevice = linDeviceDao.getByMac(mac);
            if (linDevice == null) {
                return null;
            }
            // 保存设备每次上下线消息
            linDeviceDao.saveOnlineRecord(onlineMessage, mac, linDevice.getId());
            // 设备最近在离线信息
            JSONObject deviceLastOnlinePayload = null;
            if (onlineMessage.getOnline()) {
                String[] topicItems = onlineMessage.getTopicItems();
                String node = topicItems[topicItems.length - 4];
                LOGGER.info(">>> online, mac:{}, clientId:{}, ip:{}", mac, onlineMessage.getClientId(), onlineMessage.getIpAddress());

                // 保存mac，clientId等信息，作为上线标识
                if (linDeviceDao.saveOnline(onlineMessage.getClientId(), mac, onlineMessage.getIpAddress(), node)) {
                    deviceLastOnlinePayload = new JSONObject();
                    deviceLastOnlinePayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_ON);
                }
            } else {
                LOGGER.info(">>> offline, mac:{}, clientId:{}, reason:{}", mac, onlineMessage.getClientId(), onlineMessage.getReason());

                // 通过mac和clientId 判断上次的上线情况（下线失败的处理）
                if (linDeviceDao.saveOffline(onlineMessage.getClientId(), mac)) {
                    deviceLastOnlinePayload = new JSONObject();
                    deviceLastOnlinePayload.put(OnlineMessage.PAYLOAD_ONOFF, OnlineMessage.PAYLOAD_ONOFF_OFF);
                }
            }
            // 上下线处理完成后，设备最近状态作为保留消息，供App使用
            if (deviceLastOnlinePayload == null) {
                return null;
            }
            // 保留消息的topic：/linnei/retain/
            String topic = MqttConstant.TOPIC_SPLITTER + MqttConstant.LINNEI_TOPIC_PERFIX + MqttConstant.TOPIC_SPLITTER
                    + MqttConstant.RETAIN_TOPIC_SUFFIX + MqttConstant.TOPIC_SPLITTER;
            Message<String> retainMsg = MessageBuilder
                    .withPayload(deviceLastOnlinePayload.toJSONString())
                    .setHeader(MqttHeaders.TOPIC, topic)
                    .setHeader(MqttHeaders.RETAINED, Boolean.TRUE)
                    .build();
            return retainMsg;
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, onlineMessage:{}", onlineMessage, e);
            return null;
        }
    }

}
