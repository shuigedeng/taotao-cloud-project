package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplyVo01 {

	@NotNull(message = "申请id不能为空")
	private Long applyId;

}
