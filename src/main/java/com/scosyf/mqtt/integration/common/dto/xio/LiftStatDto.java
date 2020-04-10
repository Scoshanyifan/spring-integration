package com.scosyf.mqtt.integration.common.dto.xio;


import com.scosyf.mqtt.integration.constant.xio.LiftFuncTypeEnum;

import java.io.Serializable;

/**
 * @project: xiao2020-farm-msg
 * @author: kunbu
 * @create: 2020-03-31 16:28
 **/
public class LiftStatDto implements Serializable {

    private LiftFuncTypeEnum funcType;

    private String gatewaySn;

    private String liftId;

    private String realNum;

    private String gatewayId;

    private Integer callCount;

    private Boolean liftOnline;
    /**
     * 电梯使用状态：1-正常 2-满载 3-锁梯
     */
    private Integer liftState;

    private Integer currFloor;
    private Integer door;
    private Integer direction;
    private Integer open;

    public LiftFuncTypeEnum getFuncType() {
        return funcType;
    }

    public void setFuncType(LiftFuncTypeEnum funcType) {
        this.funcType = funcType;
    }

    public String getGatewaySn() {
        return gatewaySn;
    }

    public void setGatewaySn(String gatewaySn) {
        this.gatewaySn = gatewaySn;
    }

    public String getLiftId() {
        return liftId;
    }

    public void setLiftId(String liftId) {
        this.liftId = liftId;
    }

    public String getRealNum() {
        return realNum;
    }

    public void setRealNum(String realNum) {
        this.realNum = realNum;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public Boolean getLiftOnline() {
        return liftOnline;
    }

    public void setLiftOnline(Boolean liftOnline) {
        this.liftOnline = liftOnline;
    }

    public Integer getLiftState() {
        return liftState;
    }

    public void setLiftState(Integer liftState) {
        this.liftState = liftState;
    }

    public Integer getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(Integer currFloor) {
        this.currFloor = currFloor;
    }

    public Integer getDoor() {
        return door;
    }

    public void setDoor(Integer door) {
        this.door = door;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "LiftStatDto{" +
                "funcType=" + funcType +
                ", gatewaySn='" + gatewaySn + '\'' +
                ", liftId='" + liftId + '\'' +
                ", realNum='" + realNum + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", callCount=" + callCount +
                ", liftOnline=" + liftOnline +
                ", liftState=" + liftState +
                ", currFloor=" + currFloor +
                ", door=" + door +
                ", direction=" + direction +
                ", open=" + open +
                '}';
    }
}
