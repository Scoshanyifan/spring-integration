package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:26
 * @description:
 */
public enum MsgTypeEnum {
    //

    J00,
    J02,
    J05,
    // 错误信息
    JER,
    // 响应
    BCK,

    NA,
    ;

    public static MsgTypeEnum getMsgType(String name) {
        for (MsgTypeEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
