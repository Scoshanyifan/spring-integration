package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:45
 * @description:
 */
public interface MqttConstant {
    
    String TOPIC_SPLITTER                   = "/";

    String NAME_SPLITTER                    = ":";

    Integer DEFAULT_THREAD_NUM              = Runtime.getRuntime().availableProcessors();

    int DEFAULT_CONNECTION_TIMEOUT          = 5000;
    long DEFAULT_COMPLETION_TIMEOUT         = 5000;

    int QOS_DEFAULT                         = 1;
    int QOS_0                               = 0;
    int QOS_2                               = 2;

    String CHARSET_DEFAULT                  = "utf-8";

}
