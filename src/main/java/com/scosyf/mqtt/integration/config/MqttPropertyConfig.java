package com.scosyf.mqtt.integration.config;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-12 10:19
 **/
public class MqttPropertyConfig {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static final String PROPERTIES_CONFIG = "test/mqtt.properties";

    private static Map<String, String> kvMap = new HashMap<>();

    static {
        InputStreamReader reader = null;
        try {
            InputStream in = MqttPropertyConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_CONFIG);
            reader = new InputStreamReader(in);

            Properties prop = new Properties();
            prop.load(reader);

            for (String key : prop.stringPropertyNames()) {
                kvMap.put(key, prop.getProperty(key, key));
            }
            logger.info(">>> 配置文件-{} 加载完成：{}", PROPERTIES_CONFIG, kvMap);
        } catch (Exception e) {
            logger.error(">>> MqttPropertyConfig load properties error.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(">>> MqttPropertyConfig close resource error.", e);
                }
            }
        }
    }

    public static String getHost() {
        return kvMap.get("mqtt.host");
    }

    public static String getUserName() {
        return kvMap.get("mqtt.username");
    }

    public static String getPassword() {
        return kvMap.get("mqtt.password");
    }

    public static String getClientId() {
        return kvMap.get("mqtt.clientid");
    }

    public static void printProperty() {
        logger.debug(">>> kvMap:{}", kvMap);
    }
}
