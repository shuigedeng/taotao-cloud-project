package com.taotao.cloud.member.api.dto;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员搜索DTO
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员搜索DTO")
public class MemberSearchPageDTO extends PageParam {

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
