package com.scosyf.mqtt.integration.constant.linnei;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:26
 * @description:
 */
public enum LinBizTypeEnum {
    // 运行参数
    J00,
    // 耗气量
    J05,
    // 错误信息
    JER,

    ;

    public static LinBizTypeEnum getMsgType(String name) {
        for (LinBizTypeEnum bizType : values()) {
            if (bizType.name().equals(name)) {
                return bizType;
            }
        }
        return null;
    }
}
