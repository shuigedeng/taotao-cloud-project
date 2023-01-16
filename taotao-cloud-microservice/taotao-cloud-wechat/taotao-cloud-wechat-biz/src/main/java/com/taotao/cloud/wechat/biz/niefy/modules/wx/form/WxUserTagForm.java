package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WxUserTagForm {

	private Long id;
	@NotEmpty(message = "标签名称不得为空")
	@Size(min = 1, max = 30, message = "标签名称长度必须为1-30字符")
	private String name;
}
