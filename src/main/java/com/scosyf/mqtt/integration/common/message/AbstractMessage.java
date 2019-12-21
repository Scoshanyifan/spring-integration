package com.scosyf.mqtt.integration.common.message;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-20 17:52
 **/
public abstract class AbstractMessage {

    private String topic;

    private String[] topicItems;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String[] getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(String[] topicItems) {
        this.topicItems = topicItems;
    }

    @Override
    public String toString() {
        return "AbstractMessage{" +
                "topic='" + topic + '\'' +
                ", topicItems=" + Arrays.toString(topicItems) +
                '}';
    }
}
