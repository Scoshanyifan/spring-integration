package com.scosyf.mqtt.integration.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.message.biz.*;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import com.scosyf.mqtt.integration.common.message.sys.SysMessage;
import com.scosyf.mqtt.integration.constant.BizMsgTypeEnum;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.SysMsgTypeEnum;
import com.scosyf.mqtt.integration.constant.biz.DeviceTypeEnum;
import com.scosyf.mqtt.integration.constant.biz.ProductTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;

import java.util.Map;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:38
 **/
public class MessageTransferUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransferUtil.class);

    /**
     * 转换为系统消息模型
     *
     **/
    public static SysMessage mqttMessage2SysMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received sys message, header:{}, payload:{}", headers, payload);
        try {
            JSONObject payloadJson = JSONObject.parseObject(payload);
            // $SYS/brokers/+/clients/+/connected
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
            String[] topicItems = topic.split(MqttConstant.TOPIC_SPLITTER);
            String lastTopicItem = topicItems[topicItems.length - 1];

            if (lastTopicItem.equals(SysMessage.CONNECTED) || lastTopicItem.equals(SysMessage.DISCONNECTED)) {
                OnlineMessage online = new OnlineMessage();
                online.setSysMsgTypeEnum(SysMsgTypeEnum.ONOFF);

                online.setClientId(payloadJson.getString(OnlineMessage.PAYLOAD_CLIENTID));
                online.setUserName(payloadJson.getString(OnlineMessage.PAYLOAD_USERNAME));
                online.setTimeStamp(payloadJson.getString(OnlineMessage.PAYLOAD_TIMPSTAMP));
                if (lastTopicItem.equals(SysMessage.CONNECTED)) {
                    online.setOnline(true);
                    online.setCleanSession(payloadJson.getString(OnlineMessage.PAYLOAD_CLEANSESSION));
                    online.setIpAddress(payloadJson.getString(OnlineMessage.PAYLOAD_IPADDRESS));
                    online.setProtocol(payloadJson.getString(OnlineMessage.PAYLOAD_PROTOCOL));
                    online.setConnAck(payloadJson.getString(OnlineMessage.PAYLOAD_CONNACK));
                } else {
                    online.setOnline(false);
                    online.setReason(payloadJson.getString(OnlineMessage.PAYLOAD_REASON));
                }
                return online;
            } else {
                // other sys message
                return null;
            }
        } catch (Exception e) {
            LOGGER.error(">>> mqttMessage2SysMessage 系统消息转换异常", e);
            return null;
        }
    }

    /**
     * 原始数据转换为业务消息模型
     *
     **/
    public static BizMessage mqttMessage2BizMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received biz message, header:{}, payload:{}", headers, payload);
        BizMessage bizMessage;
        try {
            //转换成消息实体
            bizMessage = payload2BizMessage(payload);
            //kunbu/biz/+/inf/
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
            String[] topicItems = topic.split(MqttConstant.TOPIC_SPLITTER);
            String bizId = topicItems[topicItems.length - 2];
            bizMessage.setBizId(bizId);
            //deviceType
            String dt = bizMessage.getDt().toUpperCase();
            bizMessage.setDeviceType(DeviceTypeEnum.valueOf(dt));
            bizMessage.setProductTypeEnum(ProductTypeEnum.prefix(dt));
        } catch (Exception e) {
            LOGGER.error(">>> mqttMessage2BizMessage 转业务数据失败.", e);
            bizMessage = new BizMessage();
            bizMessage.setBizMsgTypeEnum(BizMsgTypeEnum.NA);
        }
        return bizMessage;
    }

    private static BizMessage payload2BizMessage(String payload) {
        JSONObject payloadJson = JSON.parseObject(payload);
        String mt = payloadJson.getString(BizMessage.PAYLOAD_MESSAGE_TYPE).toUpperCase();
        BizMsgTypeEnum bizMsgTypeEnum = BizMsgTypeEnum.getMsgType(mt);
        BizMessage bizMessage;
        switch (bizMsgTypeEnum) {
            case J00:
                bizMessage = payloadJson.toJavaObject(J00Message.class);
                break;
            case J05:
                bizMessage = payloadJson.toJavaObject(J05Message.class);
                break;
            case JER:
                bizMessage = payloadJson.toJavaObject(JERMessage.class);
                break;
            default:
                bizMessage = payloadJson.toJavaObject(BizMessage.class);
                break;
        }
        bizMessage.setBizMsgTypeEnum(bizMsgTypeEnum);
        return bizMessage;
    }


}
