package com.taotao.cloud.quartz.quartz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.utils.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.maku.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 定时任务日志
*
* @author 阿沐 babamu@126.com
*/
@Data
@Schema(description = "定时任务日志")
public class ScheduleJobLogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "任务id")
	private Long jobId;

	@Schema(description = "任务名称")
	private String jobName;

	@Schema(description = "任务组名")
	private String jobGroup;

	@Schema(description = "spring bean名称")
	private String beanName;

	@Schema(description = "执行方法")
	private String method;

	@Schema(description = "参数")
	private String params;

	@Schema(description = "任务状态")
	private Integer status;

	@Schema(description = "异常信息")
	private String error;

	@Schema(description = "耗时(单位：毫秒)")
	private Integer times;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
	private Date createTime;

}
