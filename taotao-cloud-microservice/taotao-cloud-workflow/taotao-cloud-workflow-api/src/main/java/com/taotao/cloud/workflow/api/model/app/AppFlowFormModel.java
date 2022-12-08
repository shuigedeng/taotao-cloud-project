package com.taotao.cloud.workflow.api.model.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppFlowFormModel {

	@ApiModelProperty(value = "主键id")
	private String id;
	@ApiModelProperty(value = "流程名称")
	private String fullName;
	@ApiModelProperty(value = "流程分类")
	private String category;
	@ApiModelProperty(value = "图标")
	private String icon;
	@ApiModelProperty(value = "编码")
	private String enCode;
	@ApiModelProperty(value = "图标背景色")
	private String iconBackground;
	@ApiModelProperty(value = "表单类型")
	private Integer formType;
	@ApiModelProperty(value = "是否常用")
	private Boolean isData;

}
