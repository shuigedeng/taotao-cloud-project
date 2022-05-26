package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信小程序直播商品DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:24
 */
@RecordBuilder
public record GoodsInfo(
	@Schema(description = "图片mediaID")
	String coverImgUrl,

	@Schema(description = "商品名称")
	String name,

	/**
	 * 1：一口价（只需要传入price，price2不传）
	 * <p></p>
	 * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
	 * <p></p>
	 * 3：显示折扣价（price字段为原价，price2字段为现价 price和price2必传
	 */
	@Schema(description = "价格类型")
	Integer priceType,

	@Schema(description = "价格")
	BigDecimal price,

	@Schema(description = "价格2")
	BigDecimal price2,

	@Schema(description = "商品详情页的小程序路径")
	String url
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	//public GoodsInfo(Commodity commodity) {
	//	BeanUtil.copyProperties(commodity, this);
	//}
}
