package com.scosyf.mqtt.integration.common.online;

import com.scosyf.mqtt.integration.common.base.AbstractMessage;

/**
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public abstract class SysMessage extends AbstractMessage {

    /**
     * 设备上线消息
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE71160428/connected
     * payload:{"clientid":"d:rinnai:SR:01:SR:98D863BE7116044B","username":"d:rinnai:SR:01:SR:98D863BE7116",
     *          "ipaddress":"140.207.23.12","clean_sess":true,"protocol":3,"connack":0,"ts":1576829434}
     *
     * App上线消息
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:13701789682:1576548009/connected
     * payload:{"clientid":"a:rinnai:SR:01:SR:13701789682:1576548009","username":"a:rinnai:SR:01:SR:13701789682",
     *          "ipaddress":"223.104.210.10","clean_sess":true,"protocol":4,"connack":0,"ts":1576572695}
     **/
    public static final String CONNECTED        = "connected";

    /**
     * 设备离线消息
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE720003EE/disconnected
     * payload:{"clientid":"d:rinnai:SR:01:SR:98D863BE720003EE","username":"d:rinnai:SR:01:SR:98D863BE7200",
     *          "reason":"keepalive_timeout","ts":1577255378}
     *
     * 关于reason：normal/closed/keepalive_timeout
     **/
    public static final String DISCONNECTED     = "disconnected";

    private String[] clientItems;

    public String[] getClientItems() {
        return clientItems;
    }

    public void setClientItems(String[] clientItems) {
        this.clientItems = clientItems;
    }
}
