package com.scosyf.mqtt.integration.common.message;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-20 17:52
 **/
public abstract class AbstractMessage {

    private String[] topicItems;

    public String[] getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(String[] topicItems) {
        this.topicItems = topicItems;
    }

    @Override
    public String toString() {
        return "{" +
                ", topicItems=" + Arrays.toString(topicItems) +
                '}';
    }
}
