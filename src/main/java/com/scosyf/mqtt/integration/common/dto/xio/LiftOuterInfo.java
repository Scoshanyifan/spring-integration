package com.scosyf.mqtt.integration.common.dto.xio;

import java.util.List;

/**
 * @project: xiao2020-farm-web
 * @author: kunbu
 * @create: 2020-03-28 16:49
 **/
public class LiftOuterInfo extends LiftInfo {


    private List<Integer> upState;

    private List<Integer> downState;

    private List<Integer> openState;

    private boolean fulled;

    private boolean locked;

    public List<Integer> getUpState() {
        return upState;
    }

    public void setUpState(List<Integer> upState) {
        this.upState = upState;
    }

    public List<Integer> getDownState() {
        return downState;
    }

    public void setDownState(List<Integer> downState) {
        this.downState = downState;
    }

    public List<Integer> getOpenState() {
        return openState;
    }

    public void setOpenState(List<Integer> openState) {
        this.openState = openState;
    }

    public boolean isFulled() {
        return fulled;
    }

    public void setFulled(boolean fulled) {
        this.fulled = fulled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "LiftOuterInfo{" +
                "upState='" + upState + '\'' +
                ", downState='" + downState + '\'' +
                ", openState='" + openState + '\'' +
                ", fulled=" + fulled +
                ", locked='" + locked + '\'' +
                "} " + super.toString();
    }
}
