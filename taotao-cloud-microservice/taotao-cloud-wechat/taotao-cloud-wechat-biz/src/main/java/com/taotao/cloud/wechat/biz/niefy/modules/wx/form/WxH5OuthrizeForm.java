package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import com.github.niefy.common.utils.Json;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WxH5OuthrizeForm {

	@NotEmpty(message = "code不得为空")
	private String code;

	@Override
	public String toString() {
		return Json.toJsonString(this);
	}
}
