package com.scosyf.mqtt.integration.dao.xio;

import com.mongodb.client.result.UpdateResult;
import com.scosyf.mqtt.integration.common.entity.DeviceOnlineRecord;
import com.scosyf.mqtt.integration.common.entity.xio.XioDevice;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import com.scosyf.mqtt.integration.dao.linnei.LinDeviceDao;
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
public class XioDiviceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinDeviceDao.class);

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
        UpdateResult upRes = mongoTemplate.updateFirst(
                new Query(Criteria.where("sn").is(sn)),
                new Update().set("clientId", clientId).set("lastLoginIp", ip).set("online", Boolean.TRUE).set("lastOnlineTime", new Date()),
                XioDevice.class);
        return upRes.getModifiedCount() > 0;
    }

    public boolean offline(String clientId, String sn) {
        UpdateResult upRes = mongoTemplate.updateFirst(
                new Query(Criteria.where("sn").is(sn).and("clientId").is(clientId)),
                new Update().set("online", Boolean.FALSE).set("lastOfflineTime", new Date()),
                XioDevice.class);
        return upRes.getModifiedCount() > 0;
    }

}
