package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 评价数量VO
 */
@Schema(description = "评价数量VO")
public record EvaluationNumberVO(@Schema(description = "全部商品") Integer all,
                                 @Schema(description = "好评数量") Integer good,
                                 @Schema(description = "中评数量") Integer moderate,
                                 @Schema(description = "差评数量") Integer worse,
                                 @Schema(description = "有图数量") Long haveImage) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
