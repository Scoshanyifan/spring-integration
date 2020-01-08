package com.scosyf.mqtt.integration.common.message.biz;

import com.scosyf.mqtt.integration.common.message.AbstractMessage;
import com.scosyf.mqtt.integration.constant.BizMsgTypeEnum;
import com.scosyf.mqtt.integration.constant.biz.DeviceTypeEnum;
import com.scosyf.mqtt.integration.constant.biz.ProductTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:28
 **/
public class BizMessage extends AbstractMessage {

    /** 消息类型 */
    public static final String PAYLOAD_MESSAGE_TYPE         = "mt";
    /** 设备类型 */
    public static final String PAYLOAD_DEVICE_TYPE          = "dt";
    /** 时间戳 */
    public static final String PAYLOAD_LAST_TIMESTAMP       = "lt";

    private String mt;
    private String dt;
    private String lt;

    private BizMsgTypeEnum bizMsgTypeEnum;
    private DeviceTypeEnum deviceType;
    private ProductTypeEnum productTypeEnum;
    /** 业务识别码（mac） */
    private String bizId;

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

    public BizMsgTypeEnum getBizMsgTypeEnum() {
        return bizMsgTypeEnum;
    }

    public void setBizMsgTypeEnum(BizMsgTypeEnum bizMsgTypeEnum) {
        this.bizMsgTypeEnum = bizMsgTypeEnum;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public ProductTypeEnum getProductTypeEnum() {
        return productTypeEnum;
    }

    public void setProductTypeEnum(ProductTypeEnum productTypeEnum) {
        this.productTypeEnum = productTypeEnum;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public String toString() {
        return "BizMessage{" +
                "mt='" + mt + '\'' +
                ", dt='" + dt + '\'' +
                ", lt='" + lt + '\'' +
                ", bizMsgTypeEnum=" + bizMsgTypeEnum +
                ", deviceType=" + deviceType +
                ", productTypeEnum=" + productTypeEnum +
                ", bizId='" + bizId + '\'' +
                "} " + super.toString();
    }
}
