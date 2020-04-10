package com.scosyf.mqtt.integration;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.mqtt.paho.MqttHandler;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MqttTest {

//    @Test
    public void testMqttHandler() {

        JSONObject cmd = new JSONObject();
        cmd.put("cmd", "2333");

        MqttHandler.getInstance().push("/linnei/1234567890/inf/", cmd.toJSONString());

    }

}
