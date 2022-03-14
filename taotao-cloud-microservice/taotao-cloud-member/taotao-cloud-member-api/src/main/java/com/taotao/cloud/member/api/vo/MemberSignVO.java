package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员签到DTO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:31:33
 */
@Data
@Schema(description = "会员签到DTO")
public class MemberSignVO {

	@Schema(description = "会员用户名")
	private String memberName;

	@Schema(description = "会员用户ID")
	private String memberId;

	@Schema(description = "连续签到天数")
	private Integer signDay;
}
