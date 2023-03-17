package com.taotao.cloud.job.biz.quartz.vo;

import com.taotao.cloud.job.quartz.enums.QuartzJobCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 定时任务
 */
@Data
@Schema(title = "定时任务")
public class QuartzJobVO {

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 任务类名
	 */
	private String jobClassName;

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 参数
	 */
	private String parameter;

	/**
	 * 状态
	 *
	 * @see QuartzJobCode
	 */
	private Integer state;

	/**
	 * 备注
	 */
	private String remark;
}
