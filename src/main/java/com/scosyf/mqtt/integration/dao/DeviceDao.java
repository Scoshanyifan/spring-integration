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
        // 直接将本次上线信息刷新到设备表中
        UpdateResult upRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(Device.MAC).is(mac)),
                new Update().set(Device.CLIENT_ID, clientId)
                            .set(Device.IP, ip)
                            .set(Device.NODE, node)
                            .set(Device.ONLINE, Boolean.TRUE)
                            .set(Device.LAST_ONLINE_TIME, System.currentTimeMillis()),
                Device.class);
        if (upRes.getModifiedCount() <= 0) {
            LOGGER.error("saveOnline failure, clientId:{}, mac:{}, ip:{}, node:{}",
                    clientId, mac, ip, node);
        }
    }

    public boolean saveOffline(String clientId, String mac) {
        long nowTime = System.currentTimeMillis();
        // 设备表中保存最新状态（原子操作：需要通过clientId来对应上次的上线，因为消息的推送不是顺序的）
        UpdateResult offlineRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(Device.MAC).is(mac).and(Device.CLIENT_ID).is(clientId)),
                new Update().set(Device.ONLINE, Boolean.FALSE)
                            .set(Device.LAST_OFFLINE_TIME, nowTime),
                Device.class);
        Device device = mongoTemplate.findOne(
                new Query(Criteria.where(Device.MAC).is(mac)),
                Device.class);
        // 如果状态更新成功，则统计在线时长
        if (offlineRes.getModifiedCount() > 0) {
            Long lastTotalOnlineTime = device.getTotalOnlineTime();
            // lastTotalOnlineTime 需要非空判断
            long totalOnlineTime = nowTime - device.getLastOnlineTime() + (lastTotalOnlineTime == null ? 0L : lastTotalOnlineTime);
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
