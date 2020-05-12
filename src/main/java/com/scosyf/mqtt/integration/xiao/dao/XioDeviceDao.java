package com.scosyf.mqtt.integration.xiao.dao;

import com.scosyf.mqtt.integration.common.online.DeviceOnlineRecord;
import com.scosyf.mqtt.integration.common.online.OnlineMessage;
import com.scosyf.mqtt.integration.xiao.common.entity.XioDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-04-10 18:35
 **/
@Repository
public class XioDeviceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(XioDeviceDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveOnlineRecord(OnlineMessage message, String sn, String bizId) {
        DeviceOnlineRecord record = new DeviceOnlineRecord();
        record.setOnline(message.getOnline());
        record.setClientId(message.getClientId());
        record.setSn(sn);
        record.setBizId(bizId);
        record.setOnlineIp(message.getIpAddress());
        record.setOfflineReason(message.getReason());
        record.setTimeStamp(new Date());

        mongoTemplate.save(record);
    }

    public XioDevice getBySn(String sn) {
        return mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), XioDevice.class);
    }

    public boolean online(String clientId, String sn, String ip) {
        XioDevice device = mongoTemplate.findAndModify(
                Query.query(Criteria.where("sn").is(sn)),
                Update.update("clientId", clientId).set("lastLoginIp", ip).set("online", Boolean.TRUE).set("lastOnlineTime", new Date()),
                XioDevice.class);
        return device != null;

//        UpdateResult upRes = mongoTemplate.updateFirst(
//                new Query(Criteria.where("sn").is(sn)),
//                new Update().set("clientId", clientId).set("lastLoginIp", ip).set("online", Boolean.TRUE).set("lastOnlineTime", new Date()),
//                XioDevice.class);
//        return upRes.getModifiedCount() > 0;
    }

    /**
     * TODO 保证原子性
     *
     * @param clientId
     * @param sn
     * @return
     **/
    public boolean offline(String clientId, String sn) {
        XioDevice device = mongoTemplate.findAndModify(
                Query.query(Criteria.where("sn").is(sn).and("clientId").is(clientId)),
                Update.update("online", Boolean.FALSE).set("lastOfflineTime", new Date()),
                XioDevice.class);
        return device != null;

//        UpdateResult upRes = mongoTemplate.updateFirst(
//                new Query(Criteria.where("sn").is(sn).and("clientId").is(clientId)),
//                new Update().set("online", Boolean.FALSE).set("lastOfflineTime", new Date()),
//                XioDevice.class);
//        return upRes.getModifiedCount() > 0;
    }

}
