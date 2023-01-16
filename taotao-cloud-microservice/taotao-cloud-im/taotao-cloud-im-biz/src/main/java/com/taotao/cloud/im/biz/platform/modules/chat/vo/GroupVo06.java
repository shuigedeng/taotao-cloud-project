package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupVo06 {

	@NotNull(message = "群id不能为空")
	private Long groupId;

	@NotNull(message = "状态不能为空")
	private YesOrNoEnum keepGroup;
}
