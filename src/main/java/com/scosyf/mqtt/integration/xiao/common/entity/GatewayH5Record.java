package com.scosyf.mqtt.integration.xiao.common.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @project: xiao2020-farm-web
 * @author: kunbu
 * @create: 2020-03-23 16:11
 **/
@Document(value = "gateway_h5_record")
public class GatewayH5Record implements Serializable {

    @Id
    private String id;
    private String sn;
    private String h5ClientId;
    @Indexed(expireAfterSeconds = 600)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getH5ClientId() {
        return h5ClientId;
    }

    public void setH5ClientId(String h5ClientId) {
        this.h5ClientId = h5ClientId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GatewayH5Record{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", h5ClientId='" + h5ClientId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
