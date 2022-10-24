package com.taotao.cloud.job.quartz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务状态枚举
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
