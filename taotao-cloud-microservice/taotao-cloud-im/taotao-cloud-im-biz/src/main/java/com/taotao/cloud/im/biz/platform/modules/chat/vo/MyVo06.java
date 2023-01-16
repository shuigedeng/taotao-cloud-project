package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MyVo06 {

	@NotBlank(message = "微聊号不能为空")
	@Size(min = 6, max = 20, message = "微聊号长度限6-20位")
	private String chatNo;

}
