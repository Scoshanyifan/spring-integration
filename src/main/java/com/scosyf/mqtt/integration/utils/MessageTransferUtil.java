package com.scosyf.mqtt.integration.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.message.*;
import com.scosyf.mqtt.integration.constant.MqttConstant;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;
import com.scosyf.mqtt.integration.constant.TopicTypeEnum;
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

    /** ============== mqtt header ============= */

    public static final String QOS                      = "mqtt_qos";
    public static final String RECEIVED_QOS             = "mqtt_receivedQos";
    public static final String DUPLICATE                = "mqtt_duplicate";
    public static final String RETAINED                 = "mqtt_retained";
    public static final String RECEIVED_RETAINED        = "mqtt_receivedRetained";
    public static final String TOPIC                    = "mqtt_topic";
    public static final String RECEIVED_TOPIC           = "mqtt_receivedTopic";


    /**
     * 转换为系统消息模型
     *
     **/
    public static SysMessage mqttMessage2SysMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received sys message, header:{}, payload:{}", headers, payload);
        try {
            // $SYS/brokers/+/clients/+/connected
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
            String[] topicItems = topic.split(MqttConstant.TOPIC_SPLITTER);

            SysMessage sysMessage = new SysMessage();
            sysMessage.setPayload(payload);
            sysMessage.setTopic(topic);
            sysMessage.setTopicItems(topicItems);
            return sysMessage;
        } catch (Exception e) {
            LOGGER.error(">>> mqttMessage2SysMessage 系统消息转换异常", e);
            return null;
        }
    }

    /**
     * 过滤上下线消息
     * @param sysMessage
     * @return
     */
    public static Boolean filterSysMessageOnOff(SysMessage sysMessage) {
        if (sysMessage == null) {
            return Boolean.FALSE;
        }
        String onOff = sysMessage.getTopicItems()[sysMessage.getTopicItems().length - 1];
        if (MqttConstant.DISCONNECTED.equals(onOff) || MqttConstant.CONNECTED.equals(onOff)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 原始数据转换为业务消息模型
     *
     **/
    public static BizMessage mqttMessage2BizMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received biz message, header:{}, payload:{}", headers, payload);
        BizMessage bizMessage;
        try {
            //kunbu/biz/+/inf/
            String topicStr = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
            String[] topicArr = topicStr.split(MqttConstant.TOPIC_SPLITTER);
            int topicLength = topicArr.length;
            String bizId = topicArr[topicLength - 2];
            String topicType = topicArr[topicLength - 1];
            TopicTypeEnum topicTypeEnum = TopicTypeEnum.valueOf(topicType.toUpperCase());
            //转换成消息实体
            bizMessage = payload2BizMessage(payload);
            bizMessage.setBizId(bizId);
            bizMessage.setTopicTypeEnum(topicTypeEnum);
        } catch (Exception e) {
            LOGGER.error(">>> mqttMessage2BizMessage 转业务数据失败.", e);
            bizMessage = new BizMessage();
            bizMessage.setMsgTypeEnum(MsgTypeEnum.NA);
        }
        return bizMessage;
    }

    private static BizMessage payload2BizMessage(String payload) {
        JSONObject payloadJson = JSON.parseObject(payload);
        String mt = payloadJson.getString(MqttConstant.PAYLOAD_MSG_TYPE).toUpperCase();
        MsgTypeEnum msgTypeEnum = MsgTypeEnum.getMsgType(mt);
        BizMessage bizMessage;
        switch (msgTypeEnum) {
            case J00:
                bizMessage = payloadJson.toJavaObject(J00Message.class);
                break;
            case J02:
                bizMessage = payloadJson.toJavaObject(J02Message.class);
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
        bizMessage.setMsgTypeEnum(msgTypeEnum);
        return bizMessage;
    }

    public static J00Message handleJ00ByDeviceType(J00Message j00Message) {

        return null;
    }
}
