package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员积分VO
 */
@Schema(description = "会员积分VO")
public record MemberPointsHistoryVO(@Schema(description = "当前会员积分") Long point,
                                    @Schema(description = "累计获得积分") Long totalPoint) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
