package com.scosyf.mqtt.integration.constant.xio;

/**
 * @author: KunBu
 * @time: 2020/3/23 16:49
 * @description: 电梯运行状态
 */
public enum LiftStateTypeEnum {
    //
    DEFAULT(0),
    /** 正常 */
    NORMAL(1),
    /** 满载 */
    FULLED(2),
    /** 锁梯 */
    LOCKED(3),
    /** 超载 */
    OVERLOAD(4),
    /** 消防 */
    FIRE(5),

    ;

    private Integer state;

    LiftStateTypeEnum(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public static LiftStateTypeEnum getValue(Integer state) {
        for (LiftStateTypeEnum ele : values()) {
            if (ele.getState().equals(state)) {
                return ele;
            }
        }
        return DEFAULT;
    }


    public static String getDesc(boolean isOnline, Integer state) {
        if (!isOnline) {
            return "离线";
        }
        if (state == null) {
            return "在线";
        }
        switch (getValue(state)) {
            case FULLED:
                return "满载";
            case LOCKED:
                return "锁梯";
            default:
                return "正常";
        }
    }
}
