package com.scosyf.mqtt.integration.constant.linnei;

/**
 * @author: KunBu
 * @time: 2019/12/20 14:50
 * @description:
 */
public enum LinProductTypeEnum {
    //
    HEAT_OVEN(0),
    HOT_WATER(1),
    CLEANER(2),

    ;

    private int productType;

    LinProductTypeEnum(int productType) {
        this.productType = productType;
    }

    public int getProductType() {
        return productType;
    }

    public static LinProductTypeEnum of(int productType) {
        for (LinProductTypeEnum pt : values()) {
            if (pt.productType == productType) {
                return pt;
            }
        }
        return null;
    }
}
