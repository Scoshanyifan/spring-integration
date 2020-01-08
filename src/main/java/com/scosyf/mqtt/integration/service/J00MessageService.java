package com.scosyf.mqtt.integration.service;

import com.alibaba.fastjson.JSONObject;
import com.scosyf.mqtt.integration.common.entity.Device;
import com.scosyf.mqtt.integration.common.entity.DeviceInfo;
import com.scosyf.mqtt.integration.common.message.biz.J00Message;
import com.scosyf.mqtt.integration.constant.biz.ProductTypeEnum;
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
        ProductTypeEnum productType = j00Message.getProductTypeEnum();
        if (productType == null) {
            LOGGER.error(">>> handleDeviceInfo, productType null, message：{}", j00Message);
        } else {
            LOGGER.info(">>> handleDeviceInfo, message：{}", j00Message);
            Device device = convert2Device(j00Message);
            switch (productType) {
                case HOT_WATER:

                case HEAT_OVEN:

                case CLEANER:

                default:
                    break;
            }
        }
    }

    private Device convert2Device(J00Message j00Message) {
        Device device = new Device();
        device.setMac(j00Message.getBizId());
        device.setDeviceType(j00Message.getDeviceType().name());
        device.setProductType(j00Message.getProductTypeEnum().getProductType());

        JSONObject elemJson = new JSONObject();
        j00Message.getElem().stream().forEach(e -> elemJson.put(e.getKey(), e.getValue()));
        DeviceInfo deviceInfo = elemJson.toJavaObject(DeviceInfo.class);
        device.setDeviceInfo(deviceInfo);

        return device;
    }

    private void saveHotWater(Device device) {

    }

    private void saveHeatOven(Device device) {

    }

    private void saveCleaner(Device device) {

    }

}
