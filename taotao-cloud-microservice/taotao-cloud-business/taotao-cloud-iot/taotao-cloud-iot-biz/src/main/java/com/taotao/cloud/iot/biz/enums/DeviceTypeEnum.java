package com.taotao.cloud.iot.biz.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 设备类型枚举,新增的设备类型枚举命名不允许使用"_"
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum DeviceTypeEnum {
    /**
     * 手持设备
     */
    HANDSET(1),

    /**
     * 柜体
     */
    CABINET(2),

    /**
     * 传感设备
     */
    SENSOR(3);

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 解析指定的valueName
     *
     * @param valueName
     * @return DeviceTypeEnum
     * @throws IllegalArgumentException
     */
    public static DeviceTypeEnum parse(String valueName) {
        return Arrays.stream(DeviceTypeEnum.values())
                .filter(d -> d.getValue().toString().equals(valueName)
                        || d.name().equalsIgnoreCase(valueName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        StrUtil.format("无效的DeviceTypeEnum值：{}", valueName)));
    }

}
