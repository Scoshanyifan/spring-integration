package com.scosyf.mqtt.integration;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.paho.MqttHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqttTest {

    @Test
    public void testMqttHandler() {

        JSONObject cmd = new JSONObject();
        cmd.put("cmd", "2333");

        MqttHandler.getInstance().push("kunbu/biz/scosyf/inf/", cmd.toJSONString());

    }

}
