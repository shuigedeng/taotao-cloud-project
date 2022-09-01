package com.taotao.cloud.order.api.model.vo.trade;

import com.taotao.cloud.order.api.model.vo.cart.CartVO;
import com.taotao.cloud.order.api.model.vo.cart.PriceDetailVO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 整比交易对象
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "整比交易对象")
public record TradeVO(

	@Schema(description = "购物车列表")
	List<CartVO> cartList,

	@Schema(description = "购物车车计算后的总价")
	PriceDetailVO priceDetailVO
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4563542542090139404L;


}
