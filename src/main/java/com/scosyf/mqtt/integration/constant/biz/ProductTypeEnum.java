package com.scosyf.mqtt.integration.constant.biz;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: KunBu
 * @time: 2019/12/20 14:50
 * @description:
 */
public enum ProductTypeEnum {
    //
    HEAT_OVEN("0F06", 0),
    HOT_WATER("0727", 1),
    CLEANER("0F07", 2),

    ;

    private String prefix;
    private Integer productType;

    ProductTypeEnum(String prefix, Integer productType) {
        this.prefix = prefix;
        this.productType = productType;
    }

    public String getPrefix() {
        return prefix;
    }

    public Integer getProductType() {
        return productType;
    }

    public static ProductTypeEnum prefix(String deviceType) {
        if (StringUtils.isBlank(deviceType)) {
            return null;
        }
        for (ProductTypeEnum pt : values()) {
            if (deviceType.startsWith(pt.prefix)) {
                return pt;
            }
        }
        return null;
    }
}
