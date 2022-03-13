package com.taotao.cloud.goods.api.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 兑换VO
 */
@Data
public class ExchangeVO {

	@Schema(description = "是否允许积分兑换")
	private Integer enableExchange;

	@Schema(description = "兑换所需金额 ")
	private Double exchangeMoney;

	@Schema(description = "积分兑换所属分类 ")
	private Integer categoryId;

	@Schema(description = "积分兑换使用的积分 ")
	private Integer exchangePoint;
}
