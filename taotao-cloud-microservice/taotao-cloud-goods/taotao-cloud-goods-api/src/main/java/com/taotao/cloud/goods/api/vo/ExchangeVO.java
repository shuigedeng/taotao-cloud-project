package com.taotao.cloud.goods.api.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 兑换VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	@Schema(description = "是否允许积分兑换")
	private Integer enableExchange;

	@Schema(description = "兑换所需金额 ")
	private BigDecimal exchangeMoney;

	@Schema(description = "积分兑换所属分类 ")
	private Integer categoryId;

	@Schema(description = "积分兑换使用的积分 ")
	private Integer exchangePoint;
}
