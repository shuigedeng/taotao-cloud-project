/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.application.service.cart.render;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.order.application.service.order.ITradeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易构造&&创建
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:57
 */
@Service
public class TradeBuilder {

    /** 购物车渲染步骤 */
    @Autowired
    private List<ICartRenderStep> cartRenderSteps;
    /** 交易 */
    @Autowired
    private ITradeService tradeService;
    /** 购物车业务 */
    @Autowired
    private ICartService cartService;

    /**
     * 构造购物车 购物车与结算信息不一致的地方主要是优惠券计算和运费计算，其他规则都是一致都
     *
     * @param checkedWay 购物车类型
     * @return 购物车展示信息
     */
    public TradeDTO buildCart(CartTypeEnum checkedWay) {
        // 读取对应购物车的商品信息
        TradeDTO tradeDTO = cartService.readDTO(checkedWay);

        // 购物车需要将交易中的优惠券取消掉
        if (checkedWay.equals(CartTypeEnum.CART)) {
            tradeDTO.setStoreCoupons(null);
            tradeDTO.setPlatformCoupon(null);
        }

        // 按照计划进行渲染
        renderCartBySteps(tradeDTO, RenderStepStatement.cartRender);
        return tradeDTO;
    }

    /** 构造结算页面 */
    public TradeDTO buildChecked(CartTypeEnum checkedWay) {
        // 读取对应购物车的商品信息
        TradeDTO tradeDTO = cartService.readDTO(checkedWay);
        // 需要对购物车渲染
        if (isSingle(checkedWay)) {
            renderCartBySteps(tradeDTO, RenderStepStatement.checkedSingleRender);
        } else {
            renderCartBySteps(tradeDTO, RenderStepStatement.checkedRender);
        }

        return tradeDTO;
    }

    /**
     * 创建一笔交易 1.构造交易 2.创建交易
     *
     * @param checkedWay 购物车类型
     * @return 交易信息
     */
    public Trade createTrade(CartTypeEnum checkedWay) {
        // 读取对应购物车的商品信息
        TradeDTO tradeDTO = cartService.readDTO(checkedWay);

        // 需要对购物车渲染
        if (isSingle(checkedWay)) {
            renderCartBySteps(tradeDTO, RenderStepStatement.singleTradeRender);
        } else {
            renderCartBySteps(tradeDTO, RenderStepStatement.tradeRender);
        }

        // 添加order订单及order_item子订单并返回
        return tradeService.createTrade(tradeDTO);
    }

    /**
     * 是否为单品渲染
     *
     * @param checkedWay 购物车类型
     * @return 返回是否单品
     */
    private boolean isSingle(CartTypeEnum checkedWay) {
        // 拼团   积分   砍价商品

        return (checkedWay.equals(CartTypeEnum.PINTUAN)
                || checkedWay.equals(CartTypeEnum.POINTS)
                || checkedWay.equals(CartTypeEnum.KANJIA));
    }

    /**
     * 根据渲染步骤，渲染购物车信息
     *
     * @param tradeDTO 交易DTO
     * @param defaultRender 渲染枚举
     */
    private void renderCartBySteps(TradeDTO tradeDTO, RenderStepEnum[] defaultRender) {
        for (RenderStepEnum step : defaultRender) {
            for (ICartRenderStep render : cartRenderSteps) {
                try {
                    if (render.step().equals(step)) {
                        render.render(tradeDTO);
                    }
                } catch (Exception e) {
                    LogUtils.error("购物车{}渲染异常：", render.getClass(), e);
                }
            }
        }
    }
}
