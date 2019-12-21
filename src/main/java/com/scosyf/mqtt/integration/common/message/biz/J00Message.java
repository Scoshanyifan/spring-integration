package com.scosyf.mqtt.integration.common.message.biz;

import java.util.List;

/**
 * payload:{
 *      "mt":"J00",
 *      "dt":"0F060B0B",
 *      "elem":[
 *          {"id":"operationMode","data":"432804"},
 *          {"id":"burningStateDHW","data":"30"},
 *          {"id":"burningStateCH","data":"31"},
 *          {"id":"temperatureUnit","data":"30"},
 *          {"id":"waterPressureUnit","data":"31"},
 *          {"id":"hotWaterTempSetting","data":"34"},
 *          {"id":"heatingTempSetting","data":"2D"},
 *          {"id":"waterPressure","data":"0000"},
 *          {"id":"errorCode","data":"0"},
 *          {"id":"hotWaterTempBound","data":"3C25"},
 *          {"id":"heatingTempBound","data":"5228"}
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
