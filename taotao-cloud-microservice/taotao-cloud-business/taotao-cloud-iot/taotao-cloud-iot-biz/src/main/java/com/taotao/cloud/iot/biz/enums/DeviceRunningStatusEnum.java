package com.taotao.cloud.iot.biz.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 设备运行状态枚举
 *
 * @author LSF maku_lsf@163.com
 */
@Getter
@RequiredArgsConstructor
public enum DeviceRunningStatusEnum {
    /**
     * 离线状态
     */
    OFFLINE(0, "离线", DeviceEventTypeEnum.OFFLINE),

    /**
     * 在线状态
     */
    ONLINE(1, "在线", DeviceEventTypeEnum.ONLINE);

    /**
     * 状态值
     */
    private final Integer value;

    /**
     * 状态显示名称
     */
    private final String title;

    /**
     * 设备事件类型
     */
    private final DeviceEventTypeEnum eventType;


    /**
     * 解析指定的valueName
     *
     * @param valueName
     * @return DeviceRunningStatusEnum
     * @throws IllegalArgumentException
     */
    public static DeviceRunningStatusEnum parse(String valueName) {
        return Arrays.stream(DeviceRunningStatusEnum.values())
                .filter(d -> d.getValue().toString().equals(valueName)
                        || d.name().equalsIgnoreCase(valueName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        StrUtil.format("无效的DeviceRunningStatusEnum值：{}", valueName)));
    }

}
