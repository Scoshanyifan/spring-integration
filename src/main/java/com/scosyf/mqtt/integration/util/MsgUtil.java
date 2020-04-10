package com.scosyf.mqtt.integration.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scosyf.mqtt.integration.common.dto.xio.LiftInfo;
import com.scosyf.mqtt.integration.common.dto.xio.LiftInnerInfo;
import com.scosyf.mqtt.integration.common.dto.xio.LiftOuterInfo;
import com.scosyf.mqtt.integration.constant.xio.LiftFuncTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MsgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class);

    private static final int INNER_DATA_LENGTH = 12;
    private static final int OUTER_DATA_LENGTH = 24;

    public static List<LiftInfo> convertLiftData(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
//        String decryptData = AESUtil.decrypt(data);
        char[] dataCharArr = data.toCharArray();
        for (int i = 0; i < dataCharArr.length; i++) {
            LOGGER.info(">>> {}:{}", i + 1, (int)dataCharArr[i]);
        }
        List<LiftInfo> liftInfoList = new ArrayList<>();
        int start = 0;
        do {
            int func = dataCharArr[start];
            int length = dataCharArr[start + 1];
            String liftRealNum = Integer.toString(dataCharArr[start + 2]);
            int door = dataCharArr[start + 3];
            if (LiftFuncTypeEnum.inner.getType() == func) {
                // 3,8,1,0,  63,1,1,0,0,1,  [2]
                LiftInnerInfo innerInfo = new LiftInnerInfo();
                innerInfo.setFuncType(LiftFuncTypeEnum.getByType(func));
                innerInfo.setLength(length);
                innerInfo.setLiftRealNum(liftRealNum);
                // 前后门，0：前门，1：后门
                innerInfo.setDoorType(door);
                // 当前楼层，0-63
                innerInfo.setCurrFloor((int) dataCharArr[start + 4]);
                // 运行方向，0：无状状态，1：上行，2：下行
                innerInfo.setDirection((int) dataCharArr[start + 5]);
                // 开门状态，0：无状状态，1：开门 2：关门
                innerInfo.setOpen((int) dataCharArr[start + 6]);
                // 超载，0：无效状态，1：超载状态
                innerInfo.setOverload(dataCharArr[start + 7] > 0);
                // 消防，0：无效状态，1：消防状态
                innerInfo.setFire(dataCharArr[start + 8] > 0);
                // 电梯在离线，0：离线，1：在线
                innerInfo.setLiftOnline(dataCharArr[start + 9] > 0);

                List<Integer> crc = Lists.newArrayList((int) dataCharArr[start + 10], (int) dataCharArr[start + 11]);

                liftInfoList.add(innerInfo);
                start += INNER_DATA_LENGTH;
            } else if (LiftFuncTypeEnum.outer.getType() == func) {
                // 4,20,1,0,  [8],[8],0,0,  [2]
                LiftOuterInfo outerInfo = new LiftOuterInfo();
                outerInfo.setFuncType(LiftFuncTypeEnum.getByType(func));
                outerInfo.setLength(length);
                outerInfo.setLiftRealNum(liftRealNum);
                // 前后门，0：前门，1：后门
                outerInfo.setDoorType(door);
                // 上行已登记状态
                List<Integer> upState = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    upState.add((int) dataCharArr[start + i + 4]);
                }
                outerInfo.setUpState(upState);
                // 下行已登记状态
                List<Integer> downState = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    downState.add((int) dataCharArr[start + i + 12]);
                }
                outerInfo.setDownState(downState);
                // 满载，0：无效状态，1：满载状态
                outerInfo.setFulled(dataCharArr[start + 20] > 0);
                // 锁梯，0：无效状态，1：锁梯状态
                outerInfo.setLocked(dataCharArr[start + 21] > 0);

                List<Integer> crc = Lists.newArrayList((int) dataCharArr[start + 22], (int) dataCharArr[start + 23]);

                liftInfoList.add(outerInfo);
                start += OUTER_DATA_LENGTH;
            } else {

            }
        } while (start < dataCharArr.length);
        return liftInfoList;
    }

    public static List<Object> downStatData(List<String> liftRealNumList) {

        List<Byte> basicData = new ArrayList<>();
        // 前后门 (3)，0：前门，1：后门，2：前后门
        basicData.add((byte) 2);
        // 上传开关 (4)，0：关闭上传状态，1：开启上传状态
        basicData.add((byte) 1);
        // 电梯数量 (5)
        int liftCount = liftRealNumList.size();
        basicData.add((byte) liftCount);
        // 电梯物理编号 (6+N)
        for (String realNum : liftRealNumList) {
            basicData.add(Byte.parseByte(realNum));
        }

        List<Byte> downStat = new ArrayList<>();
        // 内容长度 (2)
        int dataLength = 3 + liftCount;
        downStat.add((byte) dataLength);
        downStat.addAll(basicData);

        // CRC 校验 (7+N，8+N)
        List<Byte> crcList = checkCRC(basicData, dataLength);
        downStat.addAll(crcList);

        // 功能码 (1)，0：轿内，1：厅外
        List<Byte> inner = new ArrayList<>();
        inner.add((byte) 0);
        inner.addAll(downStat);
        List<Byte> outer = new ArrayList<>();
        outer.add((byte) 1);
        outer.addAll(downStat);

        // [0, 4, 2, 1, 1, 8, 80, 10]
        byte[] innerByte = new byte[inner.size()];
        for (int i = 0; i < inner.size(); i++) {
            innerByte[i] = inner.get(i).byteValue();
        }
        byte[] outerByte = new byte[inner.size()];
        for (int i = 0; i < inner.size(); i++) {
            outerByte[i] = outer.get(i).byteValue();
        }

        List<Object> all = new ArrayList<>();
        all.add(innerByte);
        all.add(outerByte);
        return all;
//        return AESUtil.encrypt(all);
    }

    private static List<Byte> checkCRC(List<Byte> basicData, int length) {
        int crcValue = 0xFFFF;
        for (int l = 0; l < length; l++) {
            crcValue ^= basicData.get(l);
            for (int i = 0; i < 8; i++) {
                int temp = crcValue & 0x0001;
                if (temp == 1) {
                    crcValue = (crcValue >> 1) ^ 0xA001;
                } else {
                    crcValue = crcValue >> 1;
                }
            }
        }
        List<Byte> crcList = new ArrayList<>();
        // 低字节在前
        crcList.add((byte) (crcValue & 0xFF));
        crcList.add((byte) ((crcValue >> 8) & 0xFF));
        return crcList;
    }


    public static void main(String[] args) {
        // [0, 5, 2, 1, 2, 1, 127, -67, -116],[1, 5, 2, 1, 2, 1, 127, -67, -116]
        // [0, 5, 2, 1, 2, 1, 127, 189, 140],[1, 5, 2, 1, 2, 1, 127, 189, 140]
        System.out.println(JSONObject.toJSONString(downStatData(Lists.newArrayList("1", "127"))));

        // 2,7,0,2,2,4,2,2,1,3,114 >>> 0,2,2,4,2,2,1
        System.out.println(checkCRC(Lists.newArrayList((byte)0, (byte)2, (byte)2, (byte)4, (byte)2, (byte)2, (byte)1), 7));

        // 3,8,0,0,5,0,0,0,0,0,64,94

        char[] chrs = {'1', '0', 1, 0};
        for (char c : chrs) {
            System.out.println((int)c);
            System.out.println(c > 0);
        }
    }
}
