package com.taotao.cloud.order.biz.service.business.cart.render;


import com.taotao.cloud.order.api.enums.cart.RenderStepEnum;
import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;

/**
 * 购物车渲染
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:09
 */
public interface ICartRenderStep {


	/**
	 * 渲染价格步骤
	 *
	 * @return {@link RenderStepEnum }
	 * @since 2022-04-28 08:50:09
	 */
	RenderStepEnum step();

	/**
	 * 渲染一笔交易
	 *
	 * @param tradeDTO 交易DTO
	 * @since 2022-04-28 08:50:09
	 */
	void render(TradeDTO tradeDTO);


}
