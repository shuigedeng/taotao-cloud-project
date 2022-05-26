package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品购买完成信息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:21
 */
@RecordBuilder
public record GoodsCompleteMessage(
	@Schema(description = "商品id")
	Long goodsId,

	@Schema(description = "商品skuId")
	Long skuId,

	@Schema(description = "购买会员sn")
	Long memberId,

	@Schema(description = "购买数量")
	Integer buyNum
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
