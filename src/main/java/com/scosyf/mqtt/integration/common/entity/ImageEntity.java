package com.scosyf.mqtt.integration.common.entity;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-17 16:29
 **/
public class ImageEntity extends BaseEntity {

    private String imageType;
    private String imageName;
    private String imageUrl;
    private int imageSize;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "imageType='" + imageType + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageSize=" + imageSize +
                '}';
    }
}
