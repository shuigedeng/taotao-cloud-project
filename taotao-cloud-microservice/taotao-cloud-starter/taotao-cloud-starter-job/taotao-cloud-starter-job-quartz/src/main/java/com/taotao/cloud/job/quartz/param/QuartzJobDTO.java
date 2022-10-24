package com.taotao.cloud.job.quartz.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 定时任务
 */
@Data
@Accessors(chain = true)
@Schema(title = "定时任务")
public class QuartzJobDTO {

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "任务名称")
	private String jobName;

	@Schema(description = "任务组名称")
	private String groupName;

	@Schema(description = "Bean名称")
	private String beanName;

	@Schema(description = "任务类名 和 bean名称 互斥")
	private String jobClassName;

	@Schema(description = "cron表达式")
	private String cronExpression;

	@Schema(description = "方法名称")
	private String methodName;

	@Schema(description = "参数")
	private String params;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "是否并发  0：禁止  1：允许")
	private Integer concurrent;
}
