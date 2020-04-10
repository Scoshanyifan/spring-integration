package com.scosyf.mqtt.integration.constant.linnei;

public enum LinDeviceTypeEnum {
    //

    GB("0F060B0B", LinProductTypeEnum.HEAT_OVEN.getProductType()),
    G55("0F060G55", LinProductTypeEnum.HEAT_OVEN.getProductType()),

    E66("07270E66", LinProductTypeEnum.HOT_WATER.getProductType()),
    E76("07270E76", LinProductTypeEnum.HOT_WATER.getProductType()),
    E86("07270E86", LinProductTypeEnum.HOT_WATER.getProductType()),

    RWTS("0F07RWTS", LinProductTypeEnum.CLEANER.getProductType()),

    ;

    private String deviceType;

    private int productType;

    LinDeviceTypeEnum(String deviceType, int productType) {
        this.deviceType = deviceType;
        this.productType = productType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public int getProductType() {
        return productType;
    }

    public static LinDeviceTypeEnum of(String deviceType) {
        for (LinDeviceTypeEnum dt : values()) {
            if (dt.deviceType.equals(deviceType)) {
                return dt;
            }
        }
        return null;
    }
}
