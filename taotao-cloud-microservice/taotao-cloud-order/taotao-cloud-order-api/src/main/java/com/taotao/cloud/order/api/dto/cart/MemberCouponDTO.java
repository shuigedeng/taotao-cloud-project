package com.taotao.cloud.order.api.dto.cart;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用于计算优惠券结算详情
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:59
 */
@RecordBuilder
@Schema(description = "用于计算优惠券结算详情")
public record MemberCouponDTO(

	@Schema(description = "在整比交易中时： key 为店铺id，value 为每个店铺跨店优惠 结算金额 在购物车中时：key为sku id ，value为每个商品结算时的金额")
	Map<String, BigDecimal> skuDetail,

	@Schema(description = "优惠券详情")
	InnerMemberCouponDTO memberCoupon
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8276369124551043085L;

	public static class InnerMemberCouponDTO {

	}

}
