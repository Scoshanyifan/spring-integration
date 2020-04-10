package com.scosyf.mqtt.integration.xiao.dao;

import com.scosyf.mqtt.integration.xiao.common.dto.LiftStatDto;
import com.scosyf.mqtt.integration.xiao.common.entity.LiftStat;
import com.scosyf.mqtt.integration.xiao.common.constant.LiftFuncTypeEnum;
import com.scosyf.mqtt.integration.xiao.common.constant.LiftStateTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @description:
 * @author: qiudy [qiudy@hadlinks.com]
 * @create: 2020/3/28 11:00
 **/
@Repository
public class LiftStatDao {

    private static final Logger logger = LoggerFactory.getLogger(LiftStatDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean updateLiftStat(List<LiftStatDto> liftStatDtoList) {
        for (LiftStatDto liftStatDto : liftStatDtoList) {
            logger.info(">>> updateLiftStat:{}", liftStatDto);

            String liftId = liftStatDto.getLiftId();

            if (StringUtils.isNotBlank(liftId)) {
                LiftStat old = mongoTemplate.findOne(new Query(Criteria.where("liftId").is(liftId)), LiftStat.class);
                if (old != null) {
                    if (liftStatDto.getLiftState() == null) {
                        liftStatDto.setLiftState(LiftStateTypeEnum.NORMAL.getState());
                    }
                    if (liftStatDto.getFuncType().equals(LiftFuncTypeEnum.inner)) {
                        mongoTemplate.updateFirst(
                                new Query(Criteria.where("_id").is(old.getId())),
                                new Update()
                                        .set("currFloor", liftStatDto.getCurrFloor())
                                        .set("door", liftStatDto.getDoor())
                                        .set("direction", liftStatDto.getDirection())
                                        .set("open", liftStatDto.getOpen())
                                        .set("liftOnline", liftStatDto.getLiftOnline())
                                        .set("liftState", liftStatDto.getLiftState()),
                                LiftStat.class);
                    } else if (liftStatDto.getFuncType().equals(LiftFuncTypeEnum.outer)) {
                        mongoTemplate.updateFirst(
                                new Query(Criteria.where("_id").is(old.getId())),
                                new Update()
                                        .set("liftState", liftStatDto.getLiftState())
                                        .set("door", liftStatDto.getDoor()),
                                LiftStat.class);
                    } else {

                    }
                }
            }
        }
        return true;
    }

}
