package com.scosyf.mqtt.integration.common.entity.linnei;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-12-21 17:37
 **/
public class DeviceInfo {

    private String operationMode;
    private String temperatureUnit;
    private String hotWaterTempSetting;
    private String heatingTempSetting;
    private String waterPressureUnit;
    private String waterPressure;
    private String errorCode;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getHotWaterTempSetting() {
        return hotWaterTempSetting;
    }

    public void setHotWaterTempSetting(String hotWaterTempSetting) {
        this.hotWaterTempSetting = hotWaterTempSetting;
    }

    public String getHeatingTempSetting() {
        return heatingTempSetting;
    }

    public void setHeatingTempSetting(String heatingTempSetting) {
        this.heatingTempSetting = heatingTempSetting;
    }

    public String getWaterPressureUnit() {
        return waterPressureUnit;
    }

    public void setWaterPressureUnit(String waterPressureUnit) {
        this.waterPressureUnit = waterPressureUnit;
    }

    public String getWaterPressure() {
        return waterPressure;
    }

    public void setWaterPressure(String waterPressure) {
        this.waterPressure = waterPressure;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "operationMode='" + operationMode + '\'' +
                ", temperatureUnit='" + temperatureUnit + '\'' +
                ", hotWaterTempSetting='" + hotWaterTempSetting + '\'' +
                ", heatingTempSetting='" + heatingTempSetting + '\'' +
                ", waterPressureUnit='" + waterPressureUnit + '\'' +
                ", waterPressure='" + waterPressure + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
