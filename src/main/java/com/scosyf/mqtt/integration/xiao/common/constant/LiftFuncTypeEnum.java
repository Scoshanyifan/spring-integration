package com.scosyf.mqtt.integration.xiao.common.constant;

/**
 * @author: KunBu
 * @time: 2020/3/30 17:43
 * @description:
 */
public enum LiftFuncTypeEnum {
    //


    inner(3),
    outer(4),

    ;

    private int type;

    LiftFuncTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static LiftFuncTypeEnum getByType(int type) {
        for (LiftFuncTypeEnum lf : values()) {
            if (lf.type == type) {
                return lf;
            }
        }
        return null;
    }
}
