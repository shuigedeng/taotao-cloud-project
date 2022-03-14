package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员搜索VO
 */
@Data
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

}
