package com.scosyf.mqtt.integration.constant;

public enum TopicTypeEnum {
    // 下发
    down,
    // 上报
    up,
    // 查询
    query,

    ;

    public static TopicTypeEnum of(String type){
        for (TopicTypeEnum topicType : TopicTypeEnum.values()) {
            if(topicType.name().equals(type)) {
                return topicType;
            }
        }
        return null;
    }
}