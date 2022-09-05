package com.taotao.cloud.quartz.quartz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.utils.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.maku.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 定时任务
*
* @author 阿沐 babamu@126.com
*/
@Data
@Schema(description = "定时任务")
public class ScheduleJobVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "任务名称")
	private String jobName;

	@Schema(description = "任务组名")
	private String jobGroup;

	@Schema(description = "bean名称")
	private String beanName;

	@Schema(description = "执行方法")
	private String method;

	@Schema(description = "参数")
	private String params;

	@Schema(description = "cron表达式")
	private String cronExpression;

	@Schema(description = "状态 ")
	private Integer status;

	@Schema(description = "是否并发")
	private Integer concurrent;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
	private Date createTime;

}
