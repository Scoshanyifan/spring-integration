package com.scosyf.mqtt.integration.dao;

import com.mongodb.client.result.UpdateResult;
import com.scosyf.mqtt.integration.common.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveOnline(String clientId, String mac, String ip, String node) {
        UpdateResult upRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(Device.MAC).is(mac)),
                new Update().addToSet(Device.CLIENT_ID, clientId)
                            .addToSet(Device.IP, ip)
                            .addToSet(Device.NODE, node)
                            .addToSet(Device.ONLINE, Boolean.TRUE)
                            .addToSet(Device.LAST_ONLINE_TIME, System.currentTimeMillis()),
                Device.class);
        if (upRes.getModifiedCount() <= 0) {
            LOGGER.error("saveOnline failure, clientId:{}, mac:{}, ip:{}, node:{}",
                    clientId, mac, ip, node);
        }
    }

    public boolean saveOffline(String clientId, String mac) {
        long nowTime = System.currentTimeMillis();
        UpdateResult offlineRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(Device.MAC).is(mac).and(Device.CLIENT_ID).is(clientId)),
                new Update().addToSet(Device.ONLINE, Boolean.FALSE)
                            .addToSet(Device.LAST_OFFLINE_TIME, nowTime),
                Device.class);
        Device device = mongoTemplate.findOne(
                new Query(Criteria.where(Device.MAC).is(mac)),
                Device.class);
        if (offlineRes.getModifiedCount() > 0) {
            long totalOnlineTime = nowTime - device.getLastOnlineTime() + device.getTotalOnlineTime();
            mongoTemplate.updateFirst(
                    new Query(Criteria.where(Device.MAC).is(mac)),
                    Update.update(Device.TOTAL_ONLINE_TIME, totalOnlineTime),
                    Device.class);
            return true;
        } else {
            LOGGER.error("saveOffline failure, newClientId:{}, oldClientId:{}, mac:{}",
                    clientId, device.getClientId(), mac);
            return false;
        }
    }

}
