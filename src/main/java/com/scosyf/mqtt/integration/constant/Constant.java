package com.scosyf.mqtt.integration.constant;

/**
 * @author: KunBu
 * @time: 2019/10/9 17:45
 * @description:
 */
public interface Constant {
    
    int MAC_LENGTH                          = 12;
    
    int PHONE_LENGTH                        = 11;

    String TOPIC_SPLITTER                   = "/";

    String NAME_SPLITTER                    = ":";

    Integer DEFAULT_THREAD_NUM              = Runtime.getRuntime().availableProcessors();

    long DEFAULT_COMPLETION_TIMEOUT         = 5000;

    int DEFAULT_QOS                         = 1;


    /** ======================== payload ======================== */

    String PAYLOAD_MSG_TYPE                 = "msg_type";

}
