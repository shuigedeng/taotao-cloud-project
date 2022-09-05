package com.taotao.cloud.quartz.quartz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务状态枚举
 *
 * @author 阿沐 babamu@126.com
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatusEnum {
    /**
     * 暂停
     */
    PAUSE(0),
    /**
     * 正常
     */
    NORMAL(1);

    private final int value;
}
