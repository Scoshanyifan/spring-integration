package com.scosyf.mqtt.integration.common.message.sys;

import com.scosyf.mqtt.integration.constant.SysMsgTypeEnum;

import java.util.Arrays;

/**
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public abstract class SysMessage {

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:13701789682:1576548009/connected
     * payload:{"clientid":"a:rinnai:SR:01:SR:13701789682:1576548009","username":"a:rinnai:SR:01:SR:13701789682",
     *          "ipaddress":"223.104.210.10","clean_sess":true,"protocol":4,"connack":0,"ts":1576572695}
     *
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE71160428/connected
     * payload:{"clientid":"d:rinnai:SR:01:SR:98D863BE7116044B","username":"d:rinnai:SR:01:SR:98D863BE7116",
     *          "ipaddress":"140.207.23.12","clean_sess":true,"protocol":3,"connack":0,"ts":1576829434}
     **/
    public static final String CONNECTED        = "connected";

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:152019216801576557507961/disconnected
     *
     * payload:{"clientid":"a:rinnai:SR:01:SR:152019216801576557507961","username":"a:rinnai:SR:01:SR:15201921680",
     *          "reason":"closed/normal/keepalive_timeout","ts":1576572946}
     *
     **/
    public static final String DISCONNECTED     = "disconnected";

    private SysMsgTypeEnum sysMsgTypeEnum;

    private String[] topicItems;

    public String[] getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(String[] topicItems) {
        this.topicItems = topicItems;
    }

    public SysMsgTypeEnum getSysMsgTypeEnum() {
        return sysMsgTypeEnum;
    }

    public void setSysMsgTypeEnum(SysMsgTypeEnum sysMsgTypeEnum) {
        this.sysMsgTypeEnum = sysMsgTypeEnum;
    }

    @Override
    public String toString() {
        return "SysMessage{" +
                "sysMsgTypeEnum=" + sysMsgTypeEnum +
                ", topicItems=" + Arrays.toString(topicItems) +
                '}';
    }
}
