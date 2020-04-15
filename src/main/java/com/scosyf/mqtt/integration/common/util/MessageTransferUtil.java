package com.scosyf.mqtt.integration.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.linnei.common.message.BizMessage;
import com.scosyf.mqtt.integration.linnei.common.message.J00Message;
import com.scosyf.mqtt.integration.linnei.common.message.J05Message;
import com.scosyf.mqtt.integration.linnei.common.message.JERMessage;
import com.scosyf.mqtt.integration.common.online.OnlineMessage;
import com.scosyf.mqtt.integration.xiao.common.message.RawMessage;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.common.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.linnei.common.constant.LinBizTypeEnum;
import com.scosyf.mqtt.integration.linnei.common.constant.LinDeviceTypeEnum;
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
     * 转换为系统消息模型，以xio为例
     *
     **/
    public static OnlineMessage mqttMessage2OnlineMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received sys message, header >>> {}, payload >>> {}", headers, payload);
        try {
            // $SYS/brokers/emq-xiao3@10.45.33.195/clients/DEV_TYPE01_100160003190810045_0045/disconnected
            // $SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE7200:03EE/connected
            String[] topicItems = headers.get(MqttHeaders.RECEIVED_TOPIC).toString().split(MqttConstant.TOPIC_SPLITTER);
            // 检查是否为上下线消息
            String lastTopicItem = topicItems[topicItems.length - 1];
            if (OnlineMessage.CONNECTED.equals(lastTopicItem) || OnlineMessage.DISCONNECTED.equals(lastTopicItem)) {
                OnlineMessage online = new OnlineMessage();
                online.setTopicItems(topicItems);

                JSONObject payloadJson = JSONObject.parseObject(payload);
                // DEV_TYPE01_100160003190810045_0045
                // d:rinnai:SR:01:SR:98D863BE7200:03EE
                String clientId = payloadJson.getString(OnlineMessage.PAYLOAD_CLIENTID);
                online.setClientId(clientId);
                online.setClientItems(clientId.split(MqttConstant.XIAO_SPLITTER));
//                online.setClientItems(clientId.split(MqttConstant.LINNEI_SPLITTER));

                online.setUserName(payloadJson.getString(OnlineMessage.PAYLOAD_USERNAME));
                online.setTimeStamp(payloadJson.getString(OnlineMessage.PAYLOAD_TIMPSTAMP));
                if (lastTopicItem.equals(OnlineMessage.CONNECTED)) {
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
            LOGGER.error(">>> mqttMessage2OnlineMessage 系统消息转换异常", e);
            return null;
        }
    }

    /**
     * 转为原生数据
     *
     **/
    public static RawMessage mqttMessage2RawMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received raw message, header >>> {}, payload >>> {}", headers, JSONObject.toJSONString(payload));
        try {
            RawMessage rawMessage = new RawMessage();
            rawMessage.setPayload(payload);
            // topic >>> /xio/{type}/{ID}/up
            String[] topicItems = headers.get(MqttHeaders.RECEIVED_TOPIC).toString().split(MqttConstant.TOPIC_SPLITTER);
            rawMessage.setTopicItems(topicItems);
            rawMessage.setSn(topicItems[topicItems.length - 2]);
            String topicType = topicItems[topicItems.length - 1];
            TopicTypeEnum topicTypeEnum = TopicTypeEnum.of(topicType);
            rawMessage.setTopicTypeEnum(topicTypeEnum);
            return rawMessage;
        } catch (Exception e) {
            LOGGER.error("mqttMessage2RawMessage 转为原生数据", e);
            return null;
        }
    }

    /**
     * 转换为业务消息模型
     *
     **/
    public static BizMessage mqttMessage2BizMessage(String payload, Map<String, Object> headers) {
        LOGGER.info("received linnei message, header >>> {}, payload >>> {}", headers, payload);
        try {
            // 转换成具体消息类型
            BizMessage bizMessage = payload2BizMessage(payload);
            // /linnei/{mac}/query
            String[] topicItems = headers.get(MqttHeaders.RECEIVED_TOPIC).toString().split(MqttConstant.TOPIC_SPLITTER);
            bizMessage.setTopicItems(topicItems);
            bizMessage.setMac(topicItems[topicItems.length - 2]);
            // deviceType
            String dt = bizMessage.getDt().toUpperCase();
            LinDeviceTypeEnum linDeviceTypeEnum = LinDeviceTypeEnum.of(dt);
            if (linDeviceTypeEnum == null) {
                LOGGER.error(">>> mqttMessage2BizMessage 转业务数据失败, bizMessage:{}", bizMessage);
                return null;
            }
            bizMessage.setDeviceType(linDeviceTypeEnum);
            return bizMessage;
        } catch (Exception e) {
            LOGGER.error(">>> mqttMessage2BizMessage 转业务数据异常", e);
            return null;
        }
    }

    private static BizMessage payload2BizMessage(String payload) {
        JSONObject payloadJson = JSON.parseObject(payload);
        String mt = payloadJson.getString(BizMessage.PAYLOAD_MESSAGE_TYPE).toUpperCase();

        BizMessage bizMessage;
        LinBizTypeEnum bizMsgTypeEnum = LinBizTypeEnum.getMsgType(mt);
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
                return null;
        }
        bizMessage.setBizMsgTypeEnum(bizMsgTypeEnum);
        return bizMessage;
    }


}
