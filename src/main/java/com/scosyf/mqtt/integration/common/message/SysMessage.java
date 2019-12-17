package com.scosyf.mqtt.integration.common.message;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public class SysMessage {

    private String topic;
    private String payload;
    private String[] topicItems;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String[] getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(String[] topicItems) {
        this.topicItems = topicItems;
    }

    @Override
    public String toString() {
        return "SysMessage{" +
                "topic='" + topic + '\'' +
                ", payload='" + payload + '\'' +
                ", topicItems=" + Arrays.toString(topicItems) +
                '}';
    }
}
