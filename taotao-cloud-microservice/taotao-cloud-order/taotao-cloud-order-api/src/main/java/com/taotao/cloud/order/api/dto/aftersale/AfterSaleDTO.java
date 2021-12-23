package com.taotao.cloud.order.api.dto.aftersale;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 售后dto
 *
 * @since 2021/3/20 10:48
 */
@Schema(description = "售后dto")
public class AfterSaleDTO {

	@Schema(description = "订单SN")
	private String orderItemSn;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "货品ID")
	private String skuId;

	@Schema(description = "申请退款金额")
	private Double applyRefundPrice;

	@Schema(description = "申请数量")
	private Integer num;

	@Schema(description = "申请原因")
	private String reason;

	@Schema(description = "问题描述")
	private String problemDesc;

	@Schema(description = "售后图片")
	private String images;

	/**
	 * @see cn.lili.modules.order.trade.entity.enums.AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型", allowableValues = "RETURN_GOODS,EXCHANGE_GOODS,RETURN_MONEY")
	private String serviceType;

	/**
	 * @see cn.lili.modules.order.trade.entity.enums.AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	private String refundWay;

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
	private String accountType;

	@Schema(description = "银行开户行")
	private String bankDepositName;

	@Schema(description = "银行开户名")
	private String bankAccountName;

	@Schema(description = "银行卡号")
	private String bankAccountNumber;
}
