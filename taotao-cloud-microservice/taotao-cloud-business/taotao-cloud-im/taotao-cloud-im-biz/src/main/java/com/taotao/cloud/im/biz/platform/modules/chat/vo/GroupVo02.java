package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GroupVo02 {

	@NotNull(message = "群id不能为空")
	private Long groupId;

	@NotBlank(message = "群组名称不能为空")
	@Size(max = 20, message = "群组名称长度不能大于20")
	private String name;
}
