package com.taotao.cloud.workflow.api.model.home;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class NoticeVO {

	private String id;
	@JSONField(name = "title")
	private String fullName;
	private Long creatorTime;

}
