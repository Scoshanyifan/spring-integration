package com.scosyf.mqtt.integration.xiao.common.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-04-10 15:48
 **/
@Document(collection = "xio_device")
public class XioDevice {

    @Id
    private String id;
    private String sn;
    /** 冗余，业务数据中的设备id */
    private String bizId;
    private String password;
    private String clientId;
    /** TYPE01-网关 TYPE02-电梯 */
    private String deviceType;
    private Boolean online;
    private String lastLoginIp;
    private Date lastOnlineTime;
    private Date lastOfflineTime;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Date getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Date lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "XioDevice{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", bizId='" + bizId + '\'' +
                ", password='" + password + '\'' +
                ", clientId='" + clientId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", online=" + online +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastOnlineTime=" + lastOnlineTime +
                ", lastOfflineTime=" + lastOfflineTime +
                ", createTime=" + createTime +
                '}';
    }
}
