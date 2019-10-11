package com.scosyf.mqtt.integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 13:29
 **/
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    private String password;
    private String url;
    private String userName;
    private String publisherId;

    private String sysClientId;
    private String[] sysTopic;

    private String bizClientId;
    private String[] bizTopic;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getSysClientId() {
        return sysClientId;
    }

    public void setSysClientId(String sysClientId) {
        this.sysClientId = sysClientId;
    }

    public String[] getSysTopic() {
        return sysTopic;
    }

    public void setSysTopic(String[] sysTopic) {
        this.sysTopic = sysTopic;
    }

    public String getBizClientId() {
        return bizClientId;
    }

    public void setBizClientId(String bizClientId) {
        this.bizClientId = bizClientId;
    }

    public String[] getBizTopic() {
        return bizTopic;
    }

    public void setBizTopic(String[] bizTopic) {
        this.bizTopic = bizTopic;
    }
}
