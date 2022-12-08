package com.taotao.cloud.workflow.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploaderVO {

	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "请求接口")
	private String url;
	@ApiModelProperty(value = "预览文件id")
	private String fileVersionId;
}
