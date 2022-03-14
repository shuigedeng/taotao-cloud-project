package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 会员积分
 */
@Data
@Schema(description = "租户id")
public class MemberPointMessage implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "积分")
	private Long point;

	@Schema(description = "是否增加积分")
	private String type;

	@Schema(description = "会员id")
	private String memberId;
}
