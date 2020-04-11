package com.scosyf.mqtt.integration.linnei.common.message;

import com.scosyf.mqtt.integration.common.base.AbstractMessage;
import com.scosyf.mqtt.integration.linnei.common.constant.LinBizTypeEnum;
import com.scosyf.mqtt.integration.linnei.common.constant.LinDeviceTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:28
 **/
public abstract class BizMessage extends AbstractMessage {

    /** 消息类型 */
    public static final String PAYLOAD_MESSAGE_TYPE         = "mt";
    /** 设备类型 */
    public static final String PAYLOAD_DEVICE_TYPE          = "dt";
    /** 时间戳 */
    public static final String PAYLOAD_LAST_TIMESTAMP       = "lt";

    private String mt;
    private String dt;
    private String lt;

    private LinBizTypeEnum bizMsgTypeEnum;
    private LinDeviceTypeEnum deviceType;
    /** 业务识别码（mac） */
    private String mac;

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public LinBizTypeEnum getBizMsgTypeEnum() {
        return bizMsgTypeEnum;
    }

    public void setBizMsgTypeEnum(LinBizTypeEnum bizMsgTypeEnum) {
        this.bizMsgTypeEnum = bizMsgTypeEnum;
    }

    public LinDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(LinDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "BizMessage{" +
                "mt='" + mt + '\'' +
                ", dt='" + dt + '\'' +
                ", lt='" + lt + '\'' +
                ", bizMsgTypeEnum=" + bizMsgTypeEnum +
                ", deviceType=" + deviceType +
                ", mac='" + mac + '\'' +
                "} " + super.toString();
    }
}
