package com.scosyf.mqtt.integration.common.message;

import java.util.Arrays;

/**
 *
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-09 17:39
 **/
public class SysMessage extends AbstractMqttMessage {

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
        return "SysMessage{" +
                "topic='" + topic + '\'' +
                ", topicItems=" + Arrays.toString(topicItems) +
                '}';
    }
}
