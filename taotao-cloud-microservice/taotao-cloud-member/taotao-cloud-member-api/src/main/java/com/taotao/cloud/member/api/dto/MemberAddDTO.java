package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 添加会员DTO
 *
 * 
 * @since 2020/12/14 16:31
 */
@Schema(description = "添加会员DTO")
public class MemberAddDTO {

	@NotEmpty(message = "会员用户名必填")
	@Size(max = 30, message = "会员用户名最长30位")
	@Schema(description = "会员用户名")
	private String username;

	@Schema(description = "会员密码")
	private String password;

	@NotEmpty(message = "手机号码不能为空")
	@Schema(description = "手机号码", required = true)
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String mobile;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
