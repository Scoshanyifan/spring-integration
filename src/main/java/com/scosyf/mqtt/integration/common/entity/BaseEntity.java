package com.scosyf.mqtt.integration.common.entity;

import org.springframework.data.annotation.Id;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-17 18:07
 **/
public abstract class BaseEntity {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
