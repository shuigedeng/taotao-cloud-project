package com.taotao.cloud.order.api.web.vo.cart;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品促销VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "商品促销VO 购物车中")
public record GoodsPromotionVO(

	@Schema(description = "活动开始时间")
	Date startTime,

	@Schema(description = "活动结束时间")
	Date endTime,

	@Schema(description = "活动id")
	String promotionId,

	/**
	 * @see PromotionTypeEnum
	 */
	@Schema(description = "活动工具类型")
	String promotionType,

	@Schema(description = "活动名称")
	String title,

	@Schema(description = "限购数量")
	Integer limitNum
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 1622051257060817414L;

	// public GoodsPromotionVO(PromotionGoods promotionGoods) {
	// 	this.startTime = promotionGoods.getStartTime();
	// 	this.endTime = promotionGoods.getEndTime();
	// 	this.promotionId = promotionGoods.getPromotionId();
	// 	this.setPromotionType(promotionGoods.getPromotionType());
	// 	this.setLimitNum(promotionGoods.getLimitNum());
	// }

}
