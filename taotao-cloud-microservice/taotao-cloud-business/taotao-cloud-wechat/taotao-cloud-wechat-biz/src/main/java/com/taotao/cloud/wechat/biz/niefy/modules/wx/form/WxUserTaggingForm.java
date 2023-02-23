package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WxUserTaggingForm {

	@NotNull(message = "标签ID不得为空")
	private Long tagid;
}
