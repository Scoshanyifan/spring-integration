package com.scosyf.mqtt.integration.common.message;

import com.scosyf.mqtt.integration.constant.ImageTypeEnum;

import java.util.List;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 16:36
 **/
public class ImageBizMessage extends BizMessage {

    private String id;
    private List<BizElem> prop;

    private ImageTypeEnum imageType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BizElem> getProp() {
        return prop;
    }

    public void setProp(List<BizElem> prop) {
        this.prop = prop;
    }

    public ImageTypeEnum getImageType() {
        return imageType;
    }

    public void setImageType(ImageTypeEnum imageType) {
        this.imageType = imageType;
    }

    @Override
    public String toString() {
        return "ImageBizMessage{" +
                "id='" + id + '\'' +
                ", prop=" + prop +
                ", imageType=" + imageType +
                "} " + super.toString();
    }
}
