package com.taotao.cloud.order.biz.service.cart.render;


import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;

/**
 * 购物车渲染
 *
 */
public interface CartRenderStep {


    /**
     * 渲染价格步骤
     *
     * @return 渲染枚举
     */
    RenderStepEnums step();

    /**
     * 渲染一笔交易
     *
     * @param tradeDTO 交易DTO
     */
    void render(TradeDTO tradeDTO);


}
