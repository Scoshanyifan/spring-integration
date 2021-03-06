package com.scosyf.mqtt.integration.xiao.common.message;

import com.scosyf.mqtt.integration.common.base.AbstractMessage;
import com.scosyf.mqtt.integration.xiao.common.constant.XioTopicTypeEnum;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-04-10 14:47
 **/
public class RawMessage extends AbstractMessage {

    private String payload;

    private String sn;

    private XioTopicTypeEnum xioDeviceTypeEnum;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public XioTopicTypeEnum getXioDeviceTypeEnum() {
        return xioDeviceTypeEnum;
    }

    public void setXioDeviceTypeEnum(XioTopicTypeEnum xioDeviceTypeEnum) {
        this.xioDeviceTypeEnum = xioDeviceTypeEnum;
    }
}
