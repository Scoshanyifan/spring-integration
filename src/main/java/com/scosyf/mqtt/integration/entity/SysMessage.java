package com.scosyf.mqtt.integration.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public class SysMessage {

    public static final String ONLINE = "clients";
    public static final Integer ONLINE_TOPIC_LEN = 6;
    public static final Integer CLIENTS_INDEX = new Integer(3);
    public static final Set<String> ONLINE_TOKENS = new HashSet<>();
    public static final String CONNECTED = "connected";
    public static final String DISCONNECTED = "disconnected";
    public static final String ONLINE_USERNAME = "username";
    public static final String ONLINE_CLIENTID = "clientid";
    public static final String ONLINE_IP= "ipaddress";

    static {
        ONLINE_TOKENS.add(CONNECTED);
        ONLINE_TOKENS.add(DISCONNECTED);
    }

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
