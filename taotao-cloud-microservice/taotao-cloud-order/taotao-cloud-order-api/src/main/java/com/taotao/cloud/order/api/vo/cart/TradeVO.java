package com.taotao.cloud.order.api.vo.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 整比交易对象
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "整比交易对象")
public class TradeVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4563542542090139404L;

	@Schema(description = "购物车列表")
	private List<CartVO> cartList;

	@Schema(description = "购物车车计算后的总价")
	private PriceDetailVO priceDetailVO;

}
