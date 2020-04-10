package com.scosyf.mqtt.integration.common.dto.xio;


import com.scosyf.mqtt.integration.constant.xio.LiftFuncTypeEnum;

import java.io.Serializable;

/**
 * @project: xiao2020-farm-web
 * @author: kunbu
 * @create: 2020-03-30 17:36
 **/
public abstract class LiftInfo implements Serializable {

    private LiftFuncTypeEnum funcType;

    private String sn;

    private int length;

    private String liftRealNum;

    private Integer doorType;

    public Integer getDoorType() {
        return doorType;
    }

    public void setDoorType(Integer doorType) {
        this.doorType = doorType;
    }

    public LiftFuncTypeEnum getFuncType() {
        return funcType;
    }

    public void setFuncType(LiftFuncTypeEnum funcType) {
        this.funcType = funcType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLiftRealNum() {
        return liftRealNum;
    }

    public void setLiftRealNum(String liftRealNum) {
        this.liftRealNum = liftRealNum;
    }

    @Override
    public String toString() {
        return "LiftInfo{" +
                "funcType=" + funcType +
                ", sn='" + sn + '\'' +
                ", length=" + length +
                ", liftRealNum='" + liftRealNum + '\'' +
                ", doorType=" + doorType +
                '}';
    }
}
