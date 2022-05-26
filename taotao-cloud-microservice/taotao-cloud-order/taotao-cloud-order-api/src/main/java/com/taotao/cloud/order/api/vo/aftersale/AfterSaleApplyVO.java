package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 售后申请VO
 */
@Schema(description = "售后申请VO")
public record AfterSaleApplyVO(
	@Schema(description = "申请退款金额单价")
	BigDecimal applyRefundPrice,

	@Schema(description = "可申请数量")
	Integer num,

	@Schema(description = "订单子项编号")
	String orderItemSn,

	@Schema(description = "商品ID")
	Long goodsId,

	@Schema(description = "货品ID")
	Long skuId,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "商品图片")
	String image,

	@Schema(description = "商品价格")
	BigDecimal goodsPrice,

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	String refundWay,

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,MEMBERWALLET,BANKTRANSFER")
	String accountType,

	@Schema(description = "是否支持退货")
	Boolean returnGoods,

	@Schema(description = "是否支持退款")
	Boolean returnMoney,

	@Schema(description = "会员ID")
	Long memberId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
