package com.scosyf.mqtt.integration.constant.biz;

public enum DeviceTypeEnum {
    //

    GB("0F060B0B"),
    E66("07270E66"),

    ;

    private String deviceType;

    public String getDeviceType() {
        return deviceType;
    }

    DeviceTypeEnum(String deviceType) {
        this.deviceType = deviceType;
    }

    public static DeviceTypeEnum of(String deviceType) {
        for (DeviceTypeEnum dt : values()) {
            if (dt.deviceType.equals(deviceType)) {
                return dt;
            }
        }
        return null;
    }
}
