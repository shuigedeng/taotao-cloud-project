package com.taotao.cloud.im.biz.platform.modules.collect.vo;

import com.platform.modules.collect.enums.CollectTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollectVo01 {

	@NotNull(message = "收藏类型不能为空")
	private CollectTypeEnum collectType;

	@NotBlank(message = "收藏内容不能为空")
	@Size(max = 20000, message = "收藏内容长度不能大于20000")
	private String content;

}
