package com.scosyf.mqtt.integration.xiao.common.constant;

public enum XioTopicTypeEnum {
    // 通知上报
    downStat,
    // 远程登记
    downSign,
    // 上报数据
    upInfo,
    // 查询
    query,

    nil,
    ;

    public static XioTopicTypeEnum of(String type){
        for (XioTopicTypeEnum topicType : XioTopicTypeEnum.values()) {
            if(topicType.name().equals(type)) {
                return topicType;
            }
        }
        return null;
    }
}