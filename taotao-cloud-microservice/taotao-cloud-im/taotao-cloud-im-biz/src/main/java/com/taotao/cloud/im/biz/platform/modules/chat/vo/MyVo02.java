package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MyVo02 {

	@NotBlank(message = "头像不能为空")
	@Size(max = 2000, message = "头像长度不能大于2000")
	private String portrait;

}
