package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:45
 * @description:
 */
public interface MqttConstant {

    /** ================================= message ===================================== */
    
    String TOPIC_SPLITTER                           = "/";

    String NAME_SPLITTER                            = ":";

    int MAC_LENGTH                                  = 12;
    int PHONE_LENGTH                                = 11;

    String PAYLOAD_MSG_TYPE                         = "mt";
    String PAYLOAD_ONOFF                            = "onoff";
    String PAYLOAD_ONOFF_ON                         = "1";
    String PAYLOAD_ONOFF_OFF                        = "0";

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:13701789682:1576548009/connected
     *       $SYS/brokers/emqttd-linnei@10.45.33.195/clients/d:rinnai:SR:01:SR:98D863BE71160428/connected
     *
     * payload:{"clientid":"a:rinnai:SR:01:SR:13701789682:1576548009","username":"a:rinnai:SR:01:SR:13701789682",
     *          "ipaddress":"223.104.210.10","clean_sess":true,"protocol":4,"connack":0,"ts":1576572695}
     *
     **/
    String CONNECTED                                = "connected";

    /**
     * topic:$SYS/brokers/emqttd-linnei@10.45.33.195/clients/a:rinnai:SR:01:SR:152019216801576557507961/disconnected
     *
     * payload:{"clientid":"a:rinnai:SR:01:SR:152019216801576557507961","username":"a:rinnai:SR:01:SR:15201921680",
     *          "reason":"closed/normal/keepalive_timeout","ts":1576572946}
     *
     **/
    String DISCONNECTED                             = "disconnected";

    String PAYLOAD_USER_NAME                        = "username";
    String PAYLOAD_CLIENT_ID                        = "clientid";
    String PAYLOAD_TEMPSTAMP                        = "ts";
    /** connected  */
    String PAYLOAD_ONLINE_IP                        = "ipaddress";
    /** disconnected */
    String PAYLOAD_REASON                           = "reason";

    /** ================================= mqtt ===================================== */

    String DEFAULT_TOPIC_PERFIX             = "kunbu";

    Integer DEFAULT_THREAD_NUM              = Runtime.getRuntime().availableProcessors();

    int DEFAULT_CONNECTION_TIMEOUT          = 5000;
    long DEFAULT_COMPLETION_TIMEOUT         = 5000;

    int QOS_DEFAULT                         = 1;
    int QOS_0                               = 0;
    int QOS_2                               = 2;

    String CHARSET_DEFAULT                  = "utf-8";

}
