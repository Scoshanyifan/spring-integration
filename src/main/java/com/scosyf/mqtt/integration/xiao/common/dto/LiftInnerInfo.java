package com.scosyf.mqtt.integration.xiao.common.dto;

/**
 * @project: xiao2020-farm-web
 * @author: kunbu
 * @create: 2020-03-28 16:49
 **/
public class LiftInnerInfo extends LiftInfo {


    private Integer currFloor;

    private Integer direction;

    private Integer open;

    private boolean overload;

    private boolean fire;

    private boolean liftOnline;


    public Integer getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(Integer currFloor) {
        this.currFloor = currFloor;
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

    public boolean isOverload() {
        return overload;
    }

    public void setOverload(boolean overload) {
        this.overload = overload;
    }

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public boolean isLiftOnline() {
        return liftOnline;
    }

    public void setLiftOnline(boolean liftOnline) {
        this.liftOnline = liftOnline;
    }

    @Override
    public String toString() {
        return "LiftInnerInfo{" +
                ", currFloor='" + currFloor + '\'' +
                ", direction='" + direction + '\'' +
                ", open='" + open + '\'' +
                ", overload='" + overload + '\'' +
                ", fire='" + fire + '\'' +
                ", liftOnline='" + liftOnline + '\'' +
                "} " + super.toString();
    }
}
