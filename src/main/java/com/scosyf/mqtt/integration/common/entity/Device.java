package com.scosyf.mqtt.integration.common.entity;

public class Device extends BaseEntity {

    private String mac;
    private String deviceType;
    private Integer productType;
    private DeviceInfo deviceInfo;

    // ip

    // clientId

    // statInfo

    // lastOnlineTime

    // lastOfflineTime

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
