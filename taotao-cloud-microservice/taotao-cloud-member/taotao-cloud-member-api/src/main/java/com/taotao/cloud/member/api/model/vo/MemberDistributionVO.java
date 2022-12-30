package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员分布VO
 */
@Schema(description = "会员分布VO")
public record MemberDistributionVO(@Schema(description = "客户端类型") String clientEnum,
                                   @Schema(description = "数量") Integer num,
                                   @Schema(description = "比例") BigDecimal proportion) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
