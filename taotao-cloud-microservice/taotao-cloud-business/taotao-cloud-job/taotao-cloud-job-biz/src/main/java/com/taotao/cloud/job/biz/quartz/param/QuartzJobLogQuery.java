package com.taotao.cloud.job.biz.quartz.param;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "定时任务日志查询")
public class QuartzJobLogQuery extends PageQuery {

	@Schema(description = "处理器全限定名")
	private String className;

	@Schema(description = "是否执行成功")
	private Boolean success;

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
}
