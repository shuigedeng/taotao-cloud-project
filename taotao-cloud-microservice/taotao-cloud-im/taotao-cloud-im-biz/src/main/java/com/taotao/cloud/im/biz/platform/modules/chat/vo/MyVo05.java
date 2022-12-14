package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MyVo05 {

	@NotNull(message = "性别不能为空")
	private GenderEnum gender;

}
