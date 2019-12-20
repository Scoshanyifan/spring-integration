package com.scosyf.mqtt.integration.common.message;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-20 17:33
 **/
public class OnlineMessage extends SysMessage {

    public static final String clientid = "clientid";
    public static final String username = "username";
    public static final String ipaddress = "ipaddress";
    public static final String clean_sess = "clean_sess";
    public static final String protocol = "protocol";
    public static final String connack = "connack";
    public static final String ts = "ts";

    private String clientId;

    private String userName;

    private String ipAddress;

    private String cleanSess;

    private String protocl;

    private String connAck;

    private String timeStamp;

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

    public String getCleanSess() {
        return cleanSess;
    }

    public void setCleanSess(String cleanSess) {
        this.cleanSess = cleanSess;
    }

    public String getProtocl() {
        return protocl;
    }

    public void setProtocl(String protocl) {
        this.protocl = protocl;
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
}
