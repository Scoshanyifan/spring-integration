package com.scosyf.mqtt.integration.common.message.linnei;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-17 16:14
 **/
public class BizElem {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
