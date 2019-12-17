package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:26
 * @description:
 */
public enum MsgTypeEnum {
    //

    BOOK,
    MOVIE,
    IMAGE,
    MUSIC,

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
