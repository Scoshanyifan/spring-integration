package com.scosyf.mqtt.integration.xiao.service;

import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.common.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.common.mqtt.MqttPublisher;
import com.scosyf.mqtt.integration.common.online.OnlineMessage;
import com.scosyf.mqtt.integration.xiao.common.entity.XioDevice;
import com.scosyf.mqtt.integration.xiao.dao.XioDeviceDao;
import com.scosyf.mqtt.integration.xiao.util.MsgUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2020-05-08 13:20
 **/
@Component("deviceService")
public class XioDeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XioDeviceService.class);

    @Autowired
    private XioDeviceDao xioDeviceDao;

    @Autowired
    private MqttPublisher mqttPublisher;

    public XioDevice getBySn(String sn) {
        return xioDeviceDao.getBySn(sn);
    }

    public void saveOnlineRecord(OnlineMessage message, String sn, String bizId) {
        xioDeviceDao.saveOnlineRecord(message, sn, bizId);
    }

    public boolean online(String clientId, String sn, String ip) {
        return xioDeviceDao.online(clientId, sn, ip);
    }

    public boolean offline(String clientId, String sn) {
        return xioDeviceDao.offline(clientId, sn);
    }

    public void handleDown(String gatewaySn, String deviceType, boolean open) {
        XioDevice device = xioDeviceDao.getBySn(gatewaySn);
        if (device != null) {
            // TODO 模拟sn拿到电梯物理编号
            List<String> liftRealNumList = Lists.newArrayList("0", "1");
            LOGGER.info(">>> handleDown liftRealNumList:{}", liftRealNumList);
            if (CollectionUtils.isNotEmpty(liftRealNumList)) {
                // 通知网关的topic：/xio/{type}/{ID}/down
                String topic = MqttConstant.TOPIC_SPLITTER + deviceType + MqttConstant.TOPIC_SPLITTER
                        + gatewaySn + MqttConstant.TOPIC_SPLITTER + TopicTypeEnum.down.name();
                // 消息体：byte[]
                List<Object> all = MsgUtil.downStatData(liftRealNumList, open);
                for (Object data : all) {
                    try {
                        LOGGER.info(">>> handleDown sn:{}, topic:{}, payload:{}", gatewaySn, topic, data);
                        mqttPublisher.send(topic, data);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LOGGER.error(">>> sleep error", e);
                    }
                }
            }
        }
    }

}
