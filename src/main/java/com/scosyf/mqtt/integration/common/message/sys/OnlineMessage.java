package com.scosyf.mqtt.integration.common.message.sys;

/**
 * payload:{
 *      "clientid":"d:rinnai:SR:01:SR:98D863BE7116044B",
 *      "username":"d:rinnai:SR:01:SR:98D863BE7116",
 *      "ipaddress":"140.207.23.12",
 *      "clean_sess":true,
 *      "protocol":3,
 *      "connack":0,
 *      "ts":1576829434
 * }
 *
 * payload:{
 *      "clientid":"d:rinnai:SR:01:SR:98D863BE7116044B",
 *      "username":"d:rinnai:SR:01:SR:98D863BE7116",
 *      "reason":"keepalive_timeout","
 *      ts":1576829710
 * }
 *
 * @author kunbu
 *
 **/
public class OnlineMessage extends SysMessage {

    public static final String PAYLOAD_CLIENTID         = "clientid";
    public static final String PAYLOAD_USERNAME         = "username";
    public static final String PAYLOAD_TIMPSTAMP        = "ts";
    /** connected */
    public static final String PAYLOAD_IPADDRESS        = "ipaddress";
    public static final String PAYLOAD_CLEANSESSION     = "clean_sess";
    public static final String PAYLOAD_PROTOCOL         = "protocol";
    public static final String PAYLOAD_CONNACK          = "connack";
    /** disconnected reason */
    public static final String PAYLOAD_REASON           = "reason";
    /** 上下线消息处理后通知设备 */
    public static final String PAYLOAD_ONOFF            = "onoff";
    public static final String PAYLOAD_ONOFF_ON         = "1";
    public static final String PAYLOAD_ONOFF_OFF        = "0";

    private String clientId;

    private String userName;

    private String ipAddress;

    private String cleanSession;

    private String protocol;

    private String connAck;

    private String timeStamp;

    private String reason;

    private Boolean online;

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(String cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnAck() {
        return connAck;
    }

    public void setConnAck(String connAck) {
        this.connAck = connAck;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "OnlineMessage{" +
                "clientId='" + clientId + '\'' +
                ", userName='" + userName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", cleanSession='" + cleanSession + '\'' +
                ", protocol='" + protocol + '\'' +
                ", connAck='" + connAck + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", reason='" + reason + '\'' +
                ", online=" + online +
                "} " + super.toString();
    }
}
