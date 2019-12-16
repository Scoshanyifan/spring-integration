package com.scosyf.mqtt.integration.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.scosyf.mqtt.integration.constant.MqttConstant;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 14:18
 **/
public class ExecutorFactoryUtil {

    private static final Integer THREAD_NUM_SYS = MqttConstant.DEFAULT_THREAD_NUM;

    private static final Integer THREAD_NUM_BIZ = MqttConstant.DEFAULT_THREAD_NUM * 2;

    public static Executor buildSysExecutor() {
        return new ThreadPoolExecutor(
                THREAD_NUM_SYS,
                THREAD_NUM_SYS * 2,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadFactoryBuilder().setNameFormat("mqtt-sys-pool-%d").build()
        );
    }

    public static Executor buildBizExecutor() {
        return new ThreadPoolExecutor(
                THREAD_NUM_BIZ,
                THREAD_NUM_BIZ * 2,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadFactoryBuilder().setNameFormat("mqtt-biz-pool-%d").build()
        );
    }
}
