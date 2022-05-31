package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员积分
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 13:37:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "租户id")
public class MemberPointMessageDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "积分")
	private Long point;

	@Schema(description = "是否增加积分")
	private String type;

	@Schema(description = "会员id")
	private Long memberId;
}
