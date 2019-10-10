package com.scosyf.mqtt.integration.entity;

import com.scosyf.mqtt.integration.constant.MsgTopicEnum;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:28
 **/
public class BizMessage {

    public static final String PTN = "ptn";
    public static final String DEFAULT_TOPIC_PERFIX = "rinnai/SR/01/SR/";
    public static final String TOPIC_SPLIT_TOKEN = "/";
    public static final String USERNAME_SPLIT_TOKEN = ":";
    public static final String ONLINE = "online";
    public static final String ONLINE_ON = "1";
    public static final String ONLINE_OFF = "0";
    public static final int MAC_LEN = 12;

    private String topicId;
    private MsgTypeEnum msgTypeEnum;
    private MsgTopicEnum msgTopicEnum;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public MsgTypeEnum getMsgTypeEnum() {
        return msgTypeEnum;
    }

    public void setMsgTypeEnum(MsgTypeEnum msgTypeEnum) {
        this.msgTypeEnum = msgTypeEnum;
    }

    public MsgTopicEnum getMsgTopicEnum() {
        return msgTopicEnum;
    }

    public void setMsgTopicEnum(MsgTopicEnum msgTopicEnum) {
        this.msgTopicEnum = msgTopicEnum;
    }
}
