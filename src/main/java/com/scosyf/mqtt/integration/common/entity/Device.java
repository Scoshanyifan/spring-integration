package com.scosyf.mqtt.integration.common.entity;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "device")
public class Device extends BaseEntity {

    public static final String MAC                      = "mac";
    public static final String CLIENT_ID                = "clientId";
    public static final String IP                       = "lastOnlineIp";
    public static final String NODE                     = "node";
    public static final String ONLINE                   = "online";
    public static final String LAST_ONLINE_TIME         = "lastOnlineTime";
    public static final String LAST_OFFLINE_TIME        = "lastOfflineTime";
    public static final String TOTAL_ONLINE_TIME        = "totalOnlineTime";

    /** 基础信息 */
    private String mac;
    private String password;
    private String deviceType;
    private Integer productType;
    private Boolean bind;
    private Boolean add;
    private String wifiSoftVersion;
    private Integer status;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createTime;
    private Date updateTime;
    /** 在离线信息 */
    private Boolean online;
    private String node;
    private String lastOnlineIp;
    private String clientId;
    private Long lastOnlineTime;
    private Long lastOfflineTime;
    private Long totalOnlineTime;
    /** 统计信息 */
    private DeviceInfo deviceInfo;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Boolean getBind() {
        return bind;
    }

    public void setBind(Boolean bind) {
        this.bind = bind;
    }

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }

    public String getWifiSoftVersion() {
        return wifiSoftVersion;
    }

    public void setWifiSoftVersion(String wifiSoftVersion) {
        this.wifiSoftVersion = wifiSoftVersion;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getLastOnlineIp() {
        return lastOnlineIp;
    }

    public void setLastOnlineIp(String lastOnlineIp) {
        this.lastOnlineIp = lastOnlineIp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public Long getTotalOnlineTime() {
        return totalOnlineTime;
    }

    public void setTotalOnlineTime(Long totalOnlineTime) {
        this.totalOnlineTime = totalOnlineTime;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Device{" +
                "mac='" + mac + '\'' +
                ", password='" + password + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", productType=" + productType +
                ", bind=" + bind +
                ", add=" + add +
                ", wifiSoftVersion='" + wifiSoftVersion + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", online=" + online +
                ", node='" + node + '\'' +
                ", lastOnlineIp='" + lastOnlineIp + '\'' +
                ", clientId='" + clientId + '\'' +
                ", lastOnlineTime=" + lastOnlineTime +
                ", lastOfflineTime=" + lastOfflineTime +
                ", totalOnlineTime=" + totalOnlineTime +
                ", deviceInfo=" + deviceInfo +
                "} " + super.toString();
    }
}
