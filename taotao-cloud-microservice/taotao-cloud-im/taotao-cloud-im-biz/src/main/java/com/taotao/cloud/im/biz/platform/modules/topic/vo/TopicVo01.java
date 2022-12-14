package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.modules.topic.enums.TopicTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicVo01 {

	@NotNull(message = "内容类型不能为空")
	private TopicTypeEnum topicType;

	@NotBlank(message = "内容不能为空")
	@Size(max = 2000, message = "内容长度不能大于2000")
	private String content;

	@Size(max = 2000, message = "经纬度长度不能大于2000")
	private String location;

}
