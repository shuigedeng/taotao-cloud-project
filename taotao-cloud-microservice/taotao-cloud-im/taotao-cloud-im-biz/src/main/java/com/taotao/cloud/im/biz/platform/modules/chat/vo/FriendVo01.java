package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FriendVo01 {

	@NotBlank(message = "搜索参数不能为空")
	private String param;

}
