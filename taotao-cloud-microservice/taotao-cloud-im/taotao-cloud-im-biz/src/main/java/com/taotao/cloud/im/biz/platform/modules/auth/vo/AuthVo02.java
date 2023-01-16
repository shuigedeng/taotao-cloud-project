package com.taotao.cloud.im.biz.platform.modules.auth.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthVo02 {

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	private String phone;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

	/**
	 * 推送ID
	 */
	@NotBlank(message = "推送ID不能为空")
	private String cid;

}
