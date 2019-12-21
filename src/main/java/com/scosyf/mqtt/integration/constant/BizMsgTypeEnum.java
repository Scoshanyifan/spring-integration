package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:26
 * @description:
 */
public enum BizMsgTypeEnum {
    // 运行参数
    J00,
    // 耗气量
    J05,
    // 错误信息
    JER,

    NA,
    ;

    public static BizMsgTypeEnum getMsgType(String name) {
        for (BizMsgTypeEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return NA;
    }
}
