package com.scosyf.mqtt.integration.xiao.service;

import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.common.constant.MqttConstant;
import com.scosyf.mqtt.integration.common.constant.TopicTypeEnum;
import com.scosyf.mqtt.integration.common.mqtt.MqttPublisher;
import com.scosyf.mqtt.integration.common.online.OnlineMessage;
import com.scosyf.mqtt.integration.xiao.common.constant.XioDeviceTypeEnum;
import com.scosyf.mqtt.integration.xiao.common.entity.XioDevice;
import com.scosyf.mqtt.integration.xiao.dao.GatewayH5RecordDao;
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
    private GatewayH5RecordDao gatewayH5RecordDao;

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

    /**
     * 通知网关开启或关闭电梯数据上报
     * @param gatewaySn
     * @param open
     */
    public void handleDown4Gateway(String gatewaySn, boolean open) {
        XioDevice device = xioDeviceDao.getBySn(gatewaySn);
        if (device != null) {
            // TODO 模拟sn拿到电梯物理编号
            List<String> liftRealNumList = Lists.newArrayList("0", "1");
            LOGGER.info(">>> handleDown liftRealNumList:{}", liftRealNumList);
            if (CollectionUtils.isNotEmpty(liftRealNumList)) {
                // 通知网关的topic：/{type}/{ID}/down
                String topic = MqttConstant.TOPIC_SPLITTER + XioDeviceTypeEnum.TYPE01.name() + MqttConstant.TOPIC_SPLITTER
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

    /**
     * 记录当前网关下的H5连接数，用于判定何时需要关闭网关的电梯数据上报（节省流量）
     * @param gatewaySn
     * @param clientId
     * @param sign true-有H5登入
     */
    public boolean gatewaySign(String gatewaySn, String clientId, boolean sign) {
        gatewaySign(gatewaySn, clientId, sign);
        if (sign) {
            return true;
        } else {
            return gatewayH5RecordDao.checkGatewaySin(gatewaySn);
        }
    }

}
