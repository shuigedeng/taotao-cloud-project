package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendVo04 {

	@NotNull(message = "用户id不能为空")
	private Long userId;

}
