package com.scosyf.mqtt.integration.common.message;

import com.scosyf.mqtt.integration.constant.DeviceTypeEnum;
import com.scosyf.mqtt.integration.constant.ProductTypeEnum;

import java.util.List;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 16:34
 **/
public class J00Message extends BizMessage {

    private String id;
    private List<BizElem> elem;

    private DeviceTypeEnum deviceType;
    private ProductTypeEnum productTypeEnum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BizElem> getElem() {
        return elem;
    }

    public void setElem(List<BizElem> elem) {
        this.elem = elem;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public ProductTypeEnum getProductTypeEnum() {
        return productTypeEnum;
    }

    public void setProductTypeEnum(ProductTypeEnum productTypeEnum) {
        this.productTypeEnum = productTypeEnum;
    }

    @Override
    public String toString() {
        return "J00Message{" +
                "id='" + id + '\'' +
                ", elem=" + elem +
                ", deviceType=" + deviceType +
                ", productTypeEnum=" + productTypeEnum +
                "} " + super.toString();
    }
}
