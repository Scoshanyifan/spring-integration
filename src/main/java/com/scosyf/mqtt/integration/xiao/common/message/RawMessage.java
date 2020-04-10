package com.scosyf.mqtt.integration.xiao.common.message;

import com.scosyf.mqtt.integration.common.base.AbstractMessage;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-04-10 14:47
 **/
public class RawMessage extends AbstractMessage {

    private String payload;

    private String sn;

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

}
