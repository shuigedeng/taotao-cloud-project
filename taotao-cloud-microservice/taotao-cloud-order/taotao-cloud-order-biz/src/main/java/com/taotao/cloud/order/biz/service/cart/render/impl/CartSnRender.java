package com.taotao.cloud.order.biz.service.cart.render.impl;

import com.taotao.cloud.order.api.dto.cart.StoreRemarkDTO;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.biz.service.cart.render.ICartRenderStep;
import org.springframework.stereotype.Service;

/**
 * sn 生成
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:35
 */
@Service
public class CartSnRender implements ICartRenderStep {

	@Override
	public RenderStepEnums step() {
		return RenderStepEnums.CART_SN;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		//生成各个sn
		tradeDTO.setSn(SnowFlake.createStr("T"));
		tradeDTO.getCartList().forEach(item -> {
			//写入备注
			if (tradeDTO.getStoreRemark() != null) {
				for (StoreRemarkDTO remark : tradeDTO.getStoreRemark()) {
					if (item.getStoreId().equals(remark.getStoreId())) {
						item.setRemark(remark.getRemark());
					}
				}
			}
			item.setSn(SnowFlake.createStr("O"));
		});
	}
}
