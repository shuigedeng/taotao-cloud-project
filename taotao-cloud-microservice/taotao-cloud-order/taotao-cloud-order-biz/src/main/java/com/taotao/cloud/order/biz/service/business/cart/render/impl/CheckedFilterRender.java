package com.taotao.cloud.order.biz.service.business.cart.render.impl;

import com.taotao.cloud.order.api.enums.cart.RenderStepEnum;
import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.api.model.vo.cart.CartVO;
import com.taotao.cloud.order.biz.service.business.cart.render.ICartRenderStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 佣金计算
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:40
 */
@Service
public class CheckedFilterRender implements ICartRenderStep {

	@Override
	public RenderStepEnum step() {
		return RenderStepEnum.CHECKED_FILTER;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		//将购物车到sku未选择信息过滤
		List<CartSkuVO> collect = tradeDTO.getSkuList().parallelStream()
			.filter(i -> Boolean.TRUE.equals(i.getChecked())).collect(Collectors.toList());
		tradeDTO.setSkuList(collect);

		//购物车信息过滤
		List<CartVO> cartVOList = new ArrayList<>();
		//循环购物车信息
		for (CartVO cartVO : tradeDTO.getCartList()) {
			//如果商品选中，则加入到对应购物车
			cartVO.setSkuList(cartVO.getCheckedSkuList());
			cartVOList.add(cartVO);
		}
		tradeDTO.setCartList(cartVOList);
	}


}
