package com.taotao.cloud.promotion.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


/**
 * 砍价活动参与实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "砍价活动参与记录对象")
public class KanjiaActivityDTO extends KanjiaActivityLogDTO {

	@Schema(description = "砍价商品Id")
	private Long kanjiaActivityGoodsId;

}
