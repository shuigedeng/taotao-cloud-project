package com.taotao.cloud.iot.biz.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 设备属性枚举
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum DevicePropertyEnum {
    /**
     * 运行状态
     */
    RUNNING_STATUS(1),

    /**
     * APP版本
     */
    APP_VERSION(2),

    /**
     * 电池电量百分比
     */
    BATTERY_PERCENT(3),

    /**
     * 温度
     */
    TEMPERATURE(4);

    /**
     * 类型值
     */
    private final Integer value;

}
