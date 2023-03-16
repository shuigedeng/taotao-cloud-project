package com.taotao.cloud.job.biz.quartz1.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 任务执行日志
 */
@Schema(title = "定时任务执行日志")
public class QuartzJobLogVO {

	@Schema(description = "处理器名称")
	private String handlerName;

	@Schema(description = "处理器全限定名")
	private String className;

	@Schema(description = "是否执行成功")
	private Boolean success;

	@Schema(description = "错误信息")
	private String errorMessage;

	@Schema(description = "开始时间")
	private LocalDateTime startTime;

	@Schema(description = "结束时间")
	private LocalDateTime endTime;

	@Schema(description = "执行时长")
	private Long duration;

	@Schema(description = "创建时间")
	private LocalDateTime create_time;

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public LocalDateTime getCreate_time() {
		return create_time;
	}

	public void setCreate_time(LocalDateTime create_time) {
		this.create_time = create_time;
	}
}
