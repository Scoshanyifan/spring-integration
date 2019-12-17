package com.scosyf.mqtt.integration.constant;

public enum DeviceTypeEnum {

    OFO6OBOB(0),
    O7270E66(1),
    O7270E86(1),

    ;

    private int productType;

    DeviceTypeEnum(int productType) {
        this.productType = productType;
    }

    public int getProductType() {
        return productType;
    }
}
