package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评价数量VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评价数量VO")
public class EvaluationNumberVO implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "全部商品")
	private Integer all;

	@Schema(description = "好评数量")
	private Integer good;

	@Schema(description = "中评数量")
	private Integer moderate;

	@Schema(description = "差评数量")
	private Integer worse;

	@Schema(description = "有图数量")
	private Long haveImage;
}
