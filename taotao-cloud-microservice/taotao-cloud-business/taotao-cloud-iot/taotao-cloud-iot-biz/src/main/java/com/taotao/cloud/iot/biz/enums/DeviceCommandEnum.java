package com.taotao.cloud.iot.biz.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 服务端下发的指令枚举类
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum DeviceCommandEnum {

    /**
     * 远程锁定
     */
    LOCK(1, "远程锁定", DeviceEventTypeEnum.LOCK),

    /**
     * 远程解锁
     */
    UNLOCK(2, "远程解锁", DeviceEventTypeEnum.UNLOCK),

    /**
     * OTA升级
     */
    OTA_UPGRADE(3, "OTA升级", DeviceEventTypeEnum.OTA_UPGRADE);

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 显示名称
     */
    private final String title;


    /**
     * 对应的设备事件日志类型
     */
    private final DeviceEventTypeEnum eventType;

    /**
     * 解析指定的valueName
     *
     * @param valueName
     * @return DeviceCommandEnum
     * @throws IllegalArgumentException
     */
    public static DeviceCommandEnum parse(String valueName) {
        return Arrays.stream(DeviceCommandEnum.values())
                .filter(d -> d.name().equalsIgnoreCase(valueName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        StrUtil.format("无效的DeviceCommandEnum值：{}", valueName)));
    }

    /**
     * 根据 value值 获取对应枚举
     *
     * @param value
     * @return DeviceCommandEnum
     */
    public static DeviceCommandEnum getEnum(Integer value) {
        for (DeviceCommandEnum commandEnum : DeviceCommandEnum.values()) {
            if (commandEnum.getValue().equals(value)) {
                return commandEnum;
            }
        }
        return null;
    }
}

