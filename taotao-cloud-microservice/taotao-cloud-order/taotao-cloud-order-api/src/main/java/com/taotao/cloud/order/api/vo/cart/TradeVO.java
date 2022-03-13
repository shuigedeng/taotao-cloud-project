package com.taotao.cloud.order.api.vo.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 整比交易对象
 */
@Data
@Schema(description = "整比交易对象")
public class TradeVO implements Serializable {

	private static final long serialVersionUID = -4563542542090139404L;

	/**
	 * 购物车列表
	 */
	@Schema(description = "购物车列表")
	private List<CartVO> cartList;

	/**
	 * 购物车计算后的总价
	 */
	@Schema(description = "购物车车计算后的总价")
	private PriceDetailVO priceDetailVO;

	public TradeVO(List<CartVO> cartList, PriceDetailVO priceDetailVO) {
		this.cartList = cartList;
		this.priceDetailVO = priceDetailVO;
	}


}
