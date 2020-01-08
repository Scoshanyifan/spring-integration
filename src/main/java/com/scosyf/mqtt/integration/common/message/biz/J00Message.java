package com.scosyf.mqtt.integration.common.message.biz;

import java.util.List;

/**
 * payload:{
 *      "mt":"J00",
 *      "dt":"0F060B0B",
 *      "elem":[
 *          {"key":"operationMode","value":"432804"},
 *          {"key":"burningStateDHW","value":"30"},
 *          {"key":"burningStateCH","value":"31"},
 *          {"key":"temperatureUnit","value":"30"},
 *          {"key":"waterPressureUnit","value":"31"},
 *          {"key":"hotWaterTempSetting","value":"34"},
 *          {"key":"heatingTempSetting","value":"2D"},
 *          {"key":"waterPressure","value":"0000"},
 *          {"key":"errorCode","value":"0"},
 *          {"key":"hotWaterTempBound","value":"3C25"},
 *          {"key":"heatingTempBound","value":"5228"}
 *       ],
 *       "lt":"2019-12-20T09:35:30+08:00"
 * }
 *
 **/
public class J00Message extends BizMessage {

    private List<BizElem> elem;

    public List<BizElem> getElem() {
        return elem;
    }

    public void setElem(List<BizElem> elem) {
        this.elem = elem;
    }

    @Override
    public String toString() {
        return "J00Message{" +
                "elem=" + elem +
                "} " + super.toString();
    }
}
