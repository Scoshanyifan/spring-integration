package com.scosyf.mqtt.integration.common.message;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public class SysMessage {

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:13701789682:1576548009/connected
     *       $SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE71160428/connected
     *
     * payload:{"clientid":"a:rinnai:SR:01:SR:13701789682:1576548009","username":"a:rinnai:SR:01:SR:13701789682",
     *          "ipaddress":"223.104.210.10","clean_sess":true,"protocol":4,"connack":0,"ts":1576572695}
     *
     **/
    public static final String CONNECTED                                = "connected";

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:152019216801576557507961/disconnected
     *
     * payload:{"clientid":"a:rinnai:SR:01:SR:152019216801576557507961","username":"a:rinnai:SR:01:SR:15201921680",
     *          "reason":"closed/normal/keepalive_timeout","ts":1576572946}
     *
     **/
    public static final String DISCONNECTED                             = "disconnected";

    public static final String PAYLOAD_USER_NAME                        = "username";
    public static final String PAYLOAD_CLIENT_ID                        = "clientid";
    public static final String PAYLOAD_TEMPSTAMP                        = "ts";
    /** connected  */
    public static final String PAYLOAD_ONLINE_IP                        = "ipaddress";
    /** disconnected */
    public static final String PAYLOAD_REASON                           = "reason";

    public static final String ONLINE                           = "online";
    public static final String ON                               = "1";
    public static final String OFF                              = "0";


    private String topic;
    private String message;
    private String[] topicTokens;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getTopicTokens() {
        return topicTokens;
    }

    public void setTopicTokens(String[] topicTokens) {
        this.topicTokens = topicTokens;
    }

    @Override
    public String toString() {
        return "SysMessage{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                ", topicTokens=" + Arrays.toString(topicTokens) +
                '}';
    }
}
