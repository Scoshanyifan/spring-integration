/**
 * Project Name:rinnai-mqtt
 * File Name:OnlineMessageService
 * Package Name:com.bugull.mqtt.integration.service
 * Date:2018/11/610:54 AM
 * Copyright (c) 2018, 海大物联科技有限公司版权所有.
 */
package com.scosyf.mqtt.integration.common.online;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.constant.ClientTypeEnum;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.linnei.common.entity.LinDevice;
import com.scosyf.mqtt.integration.linnei.dao.LinDeviceDao;
import com.scosyf.mqtt.integration.xiao.common.constant.XioDeviceTypeEnum;
import com.scosyf.mqtt.integration.xiao.common.entity.XioDevice;
import com.scosyf.mqtt.integration.xiao.service.XioDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author kunbu
 */
@Component("onlineService")
public class OnlineMessageService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineMessageService.class);

    @Autowired
    private XioDeviceService xioDeviceService;

    @Autowired
    private LinDeviceDao linDeviceDao;

    /**
     * 记录设备上下线
     *
     * TODO 设备发送消息到消息应用，必须是经过EMQ-http认证的
     *
     * TODO 如果设备的WIFI模块换了，即mac变了，需要在后台对这台设备进行更换mac，让deviceId和mac重新配对
     *
     **/
    public Message<String> handleOnOff(OnlineMessage onlineMessage) {
        try {
            if (onlineMessage == null) {
                return null;
            }
            if (true) {
                return handleXio(onlineMessage);
            } else {
                return handleLin(onlineMessage);
            }
        } catch (Exception e) {
            LOGGER.error(">>> handleOnOff error, onlineMessage:{}", onlineMessage, e);
            return null;
        }
    }

    /**
     * xiao的逻辑：业务设备是电梯，电梯的状态信息由网关上报给消息应用
     **/
    private Message handleXio(OnlineMessage onlineMessage) {
        try {
            /**
             * DEV_{deviceType}_{sn}_{Random} >>> DEV_TYPE01_10016000319081004599_4599
             * H5_{timestamp}_{phone}_{Random}_{sn} >>> H5_1587979309276_13957130122_7361_100160003200410046
             **/
            String clientId = onlineMessage.getClientId();
            String[] clientItems = clientId.split(MqttConstant.XIAO_SPLITTER);
            // TODO 处理设备 DEV
            if (clientId.startsWith(ClientTypeEnum.DEV.name())) {
                String sn = clientItems[2];
                XioDevice xioDevice = xioDeviceService.getBySn(sn);
                if (xioDevice == null) {
                    return null;
                }
                // 保存设备每次上下线消息
                xioDeviceService.saveOnlineRecord(onlineMessage, sn, xioDevice.getBizId());
                // 处理上下线
                if (onlineMessage.getOnline()) {
                    LOGGER.info(">>> online, sn:{}, clientId:{}, ip:{}", sn, clientId, onlineMessage.getIpAddress());
                    xioDeviceService.online(onlineMessage.getClientId(), sn, onlineMessage.getIpAddress());
                    // 网关上线后，通知其上报电梯数据
                    String deviceType = clientItems[1];
                    if (XioDeviceTypeEnum.TYPE01.name().equals(deviceType)) {
                        xioDeviceService.handleDown4Gateway(sn, true);
                    }
                } else {
                    LOGGER.info(">>> offline, sn:{}, clientId:{}, reason:{}", sn, onlineMessage.getClientId(), onlineMessage.getReason());
                    xioDeviceService.offline(onlineMessage.getClientId(), sn);
                }
            } else if (clientId.startsWith(ClientTypeEnum.H5.name())) {
                // TODO 处理用户 H5
                String sn = clientItems[4];
                if (onlineMessage.getOnline()) {
                    LOGGER.info(">>> H5 online, sn:{}, clientId:{}", sn, clientId);
                    xioDeviceService.gatewaySign(sn, clientId, true);
                    // 用户上线后，通知网关开始实时上报电梯信息
                    xioDeviceService.handleDown4Gateway(sn, true);
                } else {
                    LOGGER.info(">>> H5 offline, sn:{}, clientId:{}、", sn, clientId);
                    boolean ifSign= xioDeviceService.gatewaySign(sn, clientId, false);
                    // 如果该网关在无连接，通知其关闭数据上报
                    if (!ifSign) {
                        xioDeviceService.handleDown4Gateway(sn, false);
                    }
                }
            } else {
                // TODO 其他客户端类型
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
