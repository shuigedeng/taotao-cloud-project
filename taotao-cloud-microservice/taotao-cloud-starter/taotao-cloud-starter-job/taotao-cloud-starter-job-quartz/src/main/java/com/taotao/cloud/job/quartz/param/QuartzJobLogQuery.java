package com.taotao.cloud.job.quartz.param;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Schema(title = "定时任务日志查询")
public class QuartzJobLogQuery extends PageParam {
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
