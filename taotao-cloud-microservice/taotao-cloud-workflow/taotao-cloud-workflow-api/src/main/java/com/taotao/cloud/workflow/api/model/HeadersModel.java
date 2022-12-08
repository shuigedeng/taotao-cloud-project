package com.taotao.cloud.workflow.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class HeadersModel {

	@JSONField(name = "Token")
	private String token;
	@JSONField(name = "ModuleId")
	private String moduleId;
}
