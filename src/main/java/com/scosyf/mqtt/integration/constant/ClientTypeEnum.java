package com.scosyf.mqtt.integration.constant;

/**
 * 客户端类型枚举类
 */
public enum ClientTypeEnum {
    //
    SER,
    APP,
    WX,
    H5,
    WEB,
    DEV,

    ;

    public static ClientTypeEnum of(String type) {
        for (ClientTypeEnum clientType : values()) {
            if (clientType.name().equals(type)) {
                return clientType;
            }
        }
        return null;
    }
}
