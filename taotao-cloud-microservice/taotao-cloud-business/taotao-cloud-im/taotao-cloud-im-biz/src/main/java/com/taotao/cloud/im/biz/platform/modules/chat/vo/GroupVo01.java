package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class GroupVo01 {

	@NotNull(message = "群id不能为空")
	private Long groupId;

	@NotNull(message = "好友列表不能为空")
	private List<Long> list;
}
