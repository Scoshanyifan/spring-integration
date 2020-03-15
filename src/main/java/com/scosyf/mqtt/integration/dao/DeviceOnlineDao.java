package com.scosyf.mqtt.integration.dao;

import com.scosyf.mqtt.integration.common.entity.Device;
import com.scosyf.mqtt.integration.common.entity.DeviceOnlineRecord;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-01-08 13:35
 **/
@Repository
public class DeviceOnlineDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveOnlineRecord(OnlineMessage message, Device device) {
        DeviceOnlineRecord record = new DeviceOnlineRecord();
        record.setOnline(message.getOnline());
        record.setClientId(message.getClientId());
        record.setMac(device.getMac());
        record.setDeviceId(device.getId());
        record.setOnlineIp(message.getIpAddress());
        record.setOfflineReason(message.getReason());
        record.setTimeStamp(new Date());

        mongoTemplate.save(record);
    }

}
