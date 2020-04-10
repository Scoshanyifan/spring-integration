package com.scosyf.mqtt.integration.linnei.common.message;

import java.util.List;

/**
 * payload:{
 *      "mt":"J05",
 *      "dt":"0F060B0B",
 *      "stat":[
 *          {"hotWaterGasConsumption":"0000000000000000"},
 *          {"heatingGasConsumption":"0000000000000000"}
 *      ],
 *      "lt":"2019-12-20T11:07:44+08:00"}
 **/
public class J05Message extends BizMessage {

    private List<BizElem> stat;

    public List<BizElem> getStat() {
        return stat;
    }

    public void setStat(List<BizElem> stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "J05Message{" +
                "stat=" + stat +
                "} " + super.toString();
    }
}
