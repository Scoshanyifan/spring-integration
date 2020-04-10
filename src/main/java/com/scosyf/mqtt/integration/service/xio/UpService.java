package com.scosyf.mqtt.integration.service.xio;

import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.common.dto.xio.LiftInfo;
import com.scosyf.mqtt.integration.common.dto.xio.LiftInnerInfo;
import com.scosyf.mqtt.integration.common.dto.xio.LiftOuterInfo;
import com.scosyf.mqtt.integration.common.dto.xio.LiftStatDto;
import com.scosyf.mqtt.integration.common.message.xio.RawMessage;
import com.scosyf.mqtt.integration.constant.xio.LiftStateTypeEnum;
import com.scosyf.mqtt.integration.dao.LiftStatDao;
import com.scosyf.mqtt.integration.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component("upService")
public class UpService {

    private static final Logger logger = LoggerFactory.getLogger(UpService.class);

    @Autowired
    private LiftStatDao liftStatDao;

    public Message<String> handleUp(RawMessage rawMessage) {
        try {
            List<LiftInfo> liftInfoList = MsgUtil.convertLiftData(rawMessage.getPayload());

            if (!CollectionUtils.isEmpty(liftInfoList)) {
                List<LiftStatDto> dtoList = Lists.newArrayListWithCapacity(liftInfoList.size());
                for (LiftInfo info : liftInfoList) {
                    info.setSn(rawMessage.getSn());
                    logger.info(">>> upService handle, liftInfo:{}", info);
                    switch (info.getFuncType()) {
                        case inner:
                            dtoList.add(handleInner(info));
                            break;
                        case outer:
                            dtoList.add(handleOuter(info));
                            break;
                        default:
                            break;
                    }
                }
                liftStatDao.updateLiftStat(dtoList);
            }
        } catch (Exception e) {
            logger.error(">>> upService handle error", e);
        }
        return null;
    }

    private LiftStatDto handleInner(LiftInfo info) {
        LiftInnerInfo innerInfo = (LiftInnerInfo) info;

        LiftStatDto dto = new LiftStatDto();
        dto.setGatewaySn(innerInfo.getSn());
        dto.setRealNum(innerInfo.getLiftRealNum());
        dto.setFuncType(innerInfo.getFuncType());

        dto.setCurrFloor(innerInfo.getCurrFloor());
        dto.setDirection(innerInfo.getDirection());
        dto.setDoor(innerInfo.getDoorType());
        dto.setOpen(innerInfo.getOpen());
        dto.setLiftOnline(innerInfo.isLiftOnline());
        if (innerInfo.isOverload()) {
            dto.setLiftState(LiftStateTypeEnum.OVERLOAD.getState());
        }
        if (innerInfo.isFire()) {
            dto.setLiftState(LiftStateTypeEnum.FIRE.getState());
        }
        return dto;
    }


    public LiftStatDto handleOuter(LiftInfo info) {
        LiftOuterInfo outerInfo = (LiftOuterInfo) info;

        LiftStatDto dto = new LiftStatDto();
        dto.setGatewaySn(outerInfo.getSn());
        dto.setRealNum(outerInfo.getLiftRealNum());
        dto.setFuncType(outerInfo.getFuncType());

        dto.setDoor(outerInfo.getDoorType());
        if (outerInfo.isFulled()) {
            dto.setLiftState(LiftStateTypeEnum.FULLED.getState());
        }
        if (outerInfo.isLocked()) {
            dto.setLiftState(LiftStateTypeEnum.LOCKED.getState());
        }
        return dto;
    }

}
