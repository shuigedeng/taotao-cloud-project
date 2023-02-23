package com.taotao.cloud.promotion.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;


/**
 * 促销商品数据传输对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionGoodsDTO extends PromotionGoodsBaseDTO {

	@Serial
	private static final long serialVersionUID = 9206970681612883421L;

	@Schema(description = "商品id")
	private Long goodsId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品图片")
	private String goodsImage;

	//public PromotionGoodsDTO(GoodsSku sku) {
	//    super(sku);
	//}
}
