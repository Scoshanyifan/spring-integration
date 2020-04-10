package com.scosyf.mqtt.integration.common.entity;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 设备上下线记录
 *
 * @author kunbu
 **/
@Document(collection = "device_online_record")
public class DeviceOnlineRecord extends BaseEntity {

    private String mac;
    private String sn;
    private String clientId;
    private Boolean online;
    private String onlineIp;
    private String offlineReason;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date timeStamp;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getOnlineIp() {
        return onlineIp;
    }

    public void setOnlineIp(String onlineIp) {
        this.onlineIp = onlineIp;
    }

    public String getOfflineReason() {
        return offlineReason;
    }

    public void setOfflineReason(String offlineReason) {
        this.offlineReason = offlineReason;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "DeviceOnlineRecord{" +
                "mac='" + mac + '\'' +
                ", sn='" + sn + '\'' +
                ", clientId='" + clientId + '\'' +
                ", online=" + online +
                ", onlineIp='" + onlineIp + '\'' +
                ", offlineReason='" + offlineReason + '\'' +
                ", timeStamp=" + timeStamp +
                "} " + super.toString();
    }
}
