package com.scosyf.mqtt.integration.linnei.service;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.linnei.common.entity.LinDevice;
import com.scosyf.mqtt.integration.linnei.common.entity.DeviceInfo;
import com.scosyf.mqtt.integration.linnei.common.message.J00Message;
import com.scosyf.mqtt.integration.linnei.common.constant.LinProductTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-11 09:29
 **/
@Component("j00Service")
public class J00MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(J00MessageService.class);

    public void handleDeviceInfo(J00Message j00Message) {
        try {
            LOGGER.info(">>> handleDeviceInfo, message：{}", j00Message);
            LinDevice linDevice = convert2Device(j00Message);
            LinProductTypeEnum productType = LinProductTypeEnum.of(linDevice.getProductType());
            switch (productType) {
                case HOT_WATER:

                case HEAT_OVEN:

                case CLEANER:

                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error(">>> handleDeviceInfo error", e);
        }
    }

    private LinDevice convert2Device(J00Message j00Message) {
        LinDevice linDevice = new LinDevice();
        linDevice.setMac(j00Message.getMac());
        linDevice.setDeviceType(j00Message.getDeviceType().name());
        linDevice.setProductType(j00Message.getDeviceType().getProductType());

        JSONObject elemJson = new JSONObject();
        j00Message.getElem().stream().forEach(e -> elemJson.put(e.getKey(), e.getValue()));
        DeviceInfo deviceInfo = elemJson.toJavaObject(DeviceInfo.class);
        linDevice.setDeviceInfo(deviceInfo);

        return linDevice;
    }

    private void saveHotWater(LinDevice linDevice) {

    }

    private void saveHeatOven(LinDevice linDevice) {

    }

    private void saveCleaner(LinDevice linDevice) {

    }

}
