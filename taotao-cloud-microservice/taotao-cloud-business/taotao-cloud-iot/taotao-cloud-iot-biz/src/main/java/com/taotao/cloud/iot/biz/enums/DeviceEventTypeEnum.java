package com.taotao.cloud.iot.biz.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 设备事件类型枚举
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum DeviceEventTypeEnum {
    /**
     * 下线
     */
    OFFLINE(0, "下线事件"),

    /**
     * 上线
     */
    ONLINE(1, "上线事件"),

    /**
     * 登录
     */
    SIGN_ON(2, "登录事件"),

    /**
     * 退出登录
     */
    SIGN_OFF(3, "退出登录事件"),
    /**
     * OTA升级
     */
    OTA_UPGRADE(4, "OTA升级事件"),

    /**
     * 设备远程锁定
     */
    LOCK(5, "设备远程锁定"),

    /**
     * 设备远程解锁
     */
    UNLOCK(6, "设备远程解锁"),

    /**
     * APP版本信息上报
     */
    APP_VERSION_REPORT(7, "APP版本信息上报"),

    /**
     * 电池电量上报
     */
    BATTERY_PERCENT_REPORT(8, "电池电量上报"),

    /**
     * 温度上报
     */
    TEMPERATURE_REPORT(9, "温度上报");

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String title;


    /**
     * 解析指定的valueName
     *
     * @param valueName
     * @return DeviceEventTypeEnum
     * @throws IllegalArgumentException
     */
    public static DeviceEventTypeEnum parse(String valueName) {
        return Arrays.stream(DeviceEventTypeEnum.values())
                .filter(d -> d.name().equalsIgnoreCase(valueName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        StrUtil.format("无效的DeviceEventTypeEnum值：{}", valueName)));
    }

    /**
     * 根据 value值 获取对应枚举
     *
     * @param value
     * @return DeviceCommandEnum
     */
    public static DeviceEventTypeEnum getEnum(Integer value) {
        for (DeviceEventTypeEnum commandEnum : DeviceEventTypeEnum.values()) {
            if (commandEnum.getValue().equals(value)) {
                return commandEnum;
            }
        }
        return null;
    }
}
