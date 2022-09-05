package com.taotao.cloud.quartz.quartz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务日志
 *
 * @author 阿沐 babamu@126.com
 */
@Data
@TableName("schedule_job_log")
public class ScheduleJobLogEntity {
	/**
	* id
	*/
	@TableId
	private Long id;

	/**
	* 任务id
	*/
	private Long jobId;

	/**
	* 任务名称
	*/
	private String jobName;

	/**
	* 任务组名
	*/
	private String jobGroup;

	/**
	* spring bean名称
	*/
	private String beanName;

	/**
	 * 执行方法
	 */
	private String method;

	/**
	* 参数
	*/
	private String params;

	/**
	* 任务状态
	*/
	private Integer status;

	/**
	* 异常信息
	*/
	private String error;

	/**
	* 耗时(单位：毫秒)
	*/
	private Long times;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
