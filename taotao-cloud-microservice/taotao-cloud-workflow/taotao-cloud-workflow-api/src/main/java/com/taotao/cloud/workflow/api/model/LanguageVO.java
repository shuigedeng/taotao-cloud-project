package com.taotao.cloud.workflow.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LanguageVO {

	@ApiModelProperty(value = "语言编码")
	private String encode;
	@ApiModelProperty(value = "语言名称")
	private String fullName;
}
