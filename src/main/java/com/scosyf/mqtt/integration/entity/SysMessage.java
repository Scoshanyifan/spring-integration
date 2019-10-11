package com.scosyf.mqtt.integration.entity;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public class SysMessage {

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
