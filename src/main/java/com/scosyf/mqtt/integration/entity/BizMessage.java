package com.scosyf.mqtt.integration.entity;

import com.scosyf.mqtt.integration.constant.MsgTopicEnum;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:28
 **/
public class BizMessage {

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

    @Override
    public String toString() {
        return "BizMessage{" +
                "topicId='" + topicId + '\'' +
                ", msgTypeEnum=" + msgTypeEnum +
                ", msgTopicEnum=" + msgTopicEnum +
                '}';
    }
}
