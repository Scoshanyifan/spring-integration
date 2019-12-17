package com.scosyf.mqtt.integration.common.message;

import com.scosyf.mqtt.integration.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.constant.MsgTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:28
 **/
public class BizMessage {

    /** =========================== 通用原始数据 ===========================*/

    /** 消息类型 */
    private String mt;
    /** 时间戳 */
    private String ts;

    /** =========================== 转换后的数据信息 ========================== */

    /** 业务相关id */
    private String bizId;
    /** 转换后的消息类型 */
    private MsgTypeEnum msgTypeEnum;
    /** 主题类型 */
    private TopicTypeEnum topicTypeEnum;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public MsgTypeEnum getMsgTypeEnum() {
        return msgTypeEnum;
    }

    public void setMsgTypeEnum(MsgTypeEnum msgTypeEnum) {
        this.msgTypeEnum = msgTypeEnum;
    }

    public TopicTypeEnum getTopicTypeEnum() {
        return topicTypeEnum;
    }

    public void setTopicTypeEnum(TopicTypeEnum topicTypeEnum) {
        this.topicTypeEnum = topicTypeEnum;
    }

    @Override
    public String toString() {
        return "BizMessage{" +
                "mt='" + mt + '\'' +
                ", ts='" + ts + '\'' +
                ", bizId='" + bizId + '\'' +
                ", msgTypeEnum=" + msgTypeEnum +
                ", topicTypeEnum=" + topicTypeEnum +
                '}';
    }
}
