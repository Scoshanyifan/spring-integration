package com.scosyf.mqtt.integration.common.base;

import java.util.Arrays;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-20 17:52
 **/
public abstract class AbstractMessage {

    /**
     * $SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE7116044B/disconnected
     *
     * $SYS/brokers/emq-xiao3@10.45.33.195/clients/DEV:TYPE01_100160003190810045_0045/disconnected
     */
    private String[] topicItems;

    /**
     * DEV:TYPE01_100160003190810045_0045
     *
     **/
    private String[] clientItems;


    public String[] getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(String[] topicItems) {
        this.topicItems = topicItems;
    }


    public String[] getClientItems() {
        return clientItems;
    }

    public void setClientItems(String[] clientItems) {
        this.clientItems = clientItems;
    }

    @Override
    public String toString() {
        return "AbstractMessage{" +
                "topicItems=" + Arrays.toString(topicItems) +
                ", clientItems=" + Arrays.toString(clientItems) +
                '}';
    }
}
