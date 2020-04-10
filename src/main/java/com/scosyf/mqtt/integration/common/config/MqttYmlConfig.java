package com.scosyf.mqtt.integration.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 13:29
 **/
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttYmlConfig {

    private String password;
    private String host;
    private String userName;
    private String clientId;
    private Boolean cleanSession;

    private String bizClientId;
    private String[] bizTopic;

    private String sysClientId;
    private String[] sysTopic;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
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
