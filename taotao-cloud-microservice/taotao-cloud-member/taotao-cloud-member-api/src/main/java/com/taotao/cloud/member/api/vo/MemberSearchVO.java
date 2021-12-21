package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员搜索VO
 *
 * 
 * @since 2020/12/15 10:48
 */
@Schema(description = "会员搜索VO")
public class MemberSearchVO {

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "用户手机号码")
	private String mobile;

	/**
	 * @see SwitchEnum
	 */
	@Schema(description = "会员状态")
	private String disabled;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
}
