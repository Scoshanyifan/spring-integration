package com.scosyf.mqtt.integration.xiao.dao;

import com.mongodb.client.result.DeleteResult;
import com.scosyf.mqtt.integration.common.online.DeviceOnlineRecord;
import com.scosyf.mqtt.integration.common.online.OnlineMessage;
import com.scosyf.mqtt.integration.xiao.common.entity.GatewayH5Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-04-10 18:35
 **/
@Repository
public class GatewayH5RecordDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayH5RecordDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean gatewaySign(String gatewaySn, String h5ClientId, boolean sign) {
        if (sign) {
            GatewayH5Record record = new GatewayH5Record();
            record.setSn(gatewaySn);
            record.setH5ClientId(h5ClientId);
            record.setCreateTime(new Date());
            mongoTemplate.save(record);
            return true;
        } else {
            DeleteResult delRes = mongoTemplate.remove(
                    Query.query(Criteria.where("sn").is(gatewaySn).and("h5ClientId").is(h5ClientId)),
                    GatewayH5Record.class
            );
            return delRes.getDeletedCount() > 0;
        }
    }

    public boolean checkGatewaySin(String gatewaySn) {
        long count  = mongoTemplate.count(Query.query(Criteria.where("sn").is(gatewaySn)), GatewayH5Record.class);
        return count > 0;
    }

}
