package com.taotao.cloud.quartz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务并发枚举
 */
@Getter
@AllArgsConstructor
public enum ScheduleConcurrentEnum {
    /**
     * 禁止
     */
    NO(0),
    /**
     * 允许
     */
    YES(1);

    private final int value;
}
