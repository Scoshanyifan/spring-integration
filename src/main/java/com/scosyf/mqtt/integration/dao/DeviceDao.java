package com.scosyf.mqtt.integration.dao;

import com.mongodb.client.result.UpdateResult;
import com.scosyf.mqtt.integration.common.entity.DeviceOnlineRecord;
import com.scosyf.mqtt.integration.common.entity.linnei.LinDevice;
import com.scosyf.mqtt.integration.common.message.sys.OnlineMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class DeviceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveOnlineRecord(OnlineMessage message, String mac) {
        DeviceOnlineRecord record = new DeviceOnlineRecord();
        record.setOnline(message.getOnline());
        record.setClientId(message.getClientId());
        record.setMac(mac);
        record.setOnlineIp(message.getIpAddress());
        record.setOfflineReason(message.getReason());
        record.setTimeStamp(new Date());

        mongoTemplate.save(record);
    }

    public boolean saveOnline(String clientId, String mac, String ip, String node) {
        // 直接将本次上线信息刷新到设备表中
        UpdateResult upRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(LinDevice.MAC).is(mac)),
                new Update().set(LinDevice.CLIENT_ID, clientId)
                            .set(LinDevice.IP, ip)
                            .set(LinDevice.NODE, node)
                            .set(LinDevice.ONLINE, Boolean.TRUE)
                            .set(LinDevice.LAST_ONLINE_TIME, System.currentTimeMillis()),
                LinDevice.class);
        if (upRes.getModifiedCount() <= 0) {
            LOGGER.error("saveOnline failure, clientId:{}, mac:{}, ip:{}, node:{}", clientId, mac, ip, node);
            return false;
        }
        return true;
    }

    public boolean saveOffline(String clientId, String mac) {
        long nowTime = System.currentTimeMillis();
        // 设备表中保存最新状态（原子操作：需要通过clientId来对应上次的上线，因为消息的上报不是顺序的）
        UpdateResult offlineRes = mongoTemplate.updateFirst(
                new Query(Criteria.where(LinDevice.MAC).is(mac).and(LinDevice.CLIENT_ID).is(clientId)),
                new Update().set(LinDevice.ONLINE, Boolean.FALSE)
                            .set(LinDevice.LAST_OFFLINE_TIME, nowTime),
                LinDevice.class);
        LinDevice linDevice = mongoTemplate.findOne(
                new Query(Criteria.where(LinDevice.MAC).is(mac)),
                LinDevice.class);
        // 如果状态更新成功，则统计在线时长
        if (offlineRes.getModifiedCount() > 0) {
            Long lastTotalOnlineTime = linDevice.getTotalOnlineTime();
            // lastTotalOnlineTime 需要非空判断
            long totalOnlineTime = nowTime - linDevice.getLastOnlineTime() + (lastTotalOnlineTime == null ? 0L : lastTotalOnlineTime);
            mongoTemplate.updateFirst(
                    new Query(Criteria.where(LinDevice.MAC).is(mac)),
                    Update.update(LinDevice.TOTAL_ONLINE_TIME, totalOnlineTime),
                    LinDevice.class);
            return true;
        } else {
            LOGGER.error("saveOffline failure, newClientId:{}, oldClientId:{}, mac:{}", clientId, linDevice.getClientId(), mac);
            return false;
        }
    }




}
