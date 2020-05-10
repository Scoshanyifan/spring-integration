package com.scosyf.mqtt.integration.xiao.common.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @project: xiao2020-farm-web
 * @author: kunbu
 * @create: 2020-03-23 16:11
 **/
@Document(value = "lift_stat")
public class LiftStat implements Serializable {

    @Id
    private String id;
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

    /**
     * 坐标
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    @Override
    public String toString() {
        return "LiftStat{" +
                "id='" + id + '\'' +
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
                ", location=" + location +
                '}';
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public String getRealNum() {
        return realNum;
    }

    public void setRealNum(String realNum) {
        this.realNum = realNum;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiftId() {
        return liftId;
    }

    public void setLiftId(String liftId) {
        this.liftId = liftId;
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

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }
}
