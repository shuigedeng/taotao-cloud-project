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


/**
 * 价格渲染 步骤声明
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:02
 */
public class RenderStepStatement {

    /** 购物车购物车渲染 校验商品 》 满优惠渲染 》 渲染优惠 》计算价格 */
    public static RenderStepEnum[] cartRender = {
        RenderStepEnum.CHECK_DATA, RenderStepEnum.SKU_PROMOTION, RenderStepEnum.FULL_DISCOUNT, RenderStepEnum.CART_PRICE
    };

    /** 结算页渲染 过滤选择的商品 》 校验商品 》 满优惠渲染 》 渲染优惠 》 优惠券渲染 》 计算运费 》 计算价格 */
    public static RenderStepEnum[] checkedRender = {
        RenderStepEnum.CHECKED_FILTER,
        RenderStepEnum.CHECK_DATA,
        RenderStepEnum.SKU_PROMOTION,
        RenderStepEnum.FULL_DISCOUNT,
        RenderStepEnum.COUPON,
        RenderStepEnum.SKU_FREIGHT,
        RenderStepEnum.CART_PRICE,
    };

    /** 单个商品优惠，不需要渲染满减优惠 用于特殊场景：例如积分商品，拼团商品，虚拟商品等等 */
    public static RenderStepEnum[] checkedSingleRender = {
        RenderStepEnum.CHECK_DATA, RenderStepEnum.SKU_PROMOTION, RenderStepEnum.SKU_FREIGHT, RenderStepEnum.CART_PRICE
    };

    /** 交易创建前渲染 渲染购物车 生成SN 》分销人员佣金渲染 》平台佣金渲染 */
    public static RenderStepEnum[] singleTradeRender = {
        RenderStepEnum.CHECK_DATA,
        RenderStepEnum.SKU_PROMOTION,
        RenderStepEnum.SKU_FREIGHT,
        RenderStepEnum.CART_PRICE,
        RenderStepEnum.CART_SN,
        RenderStepEnum.DISTRIBUTION,
        RenderStepEnum.PLATFORM_COMMISSION
    };

    /** 交易创建前渲染 渲染购物车 生成SN 》分销人员佣金渲染 》平台佣金渲染 */
    public static RenderStepEnum[] tradeRender = {
        RenderStepEnum.CHECKED_FILTER,
        RenderStepEnum.CHECK_DATA,
        RenderStepEnum.SKU_PROMOTION,
        RenderStepEnum.FULL_DISCOUNT,
        RenderStepEnum.COUPON,
        RenderStepEnum.SKU_FREIGHT,
        RenderStepEnum.CART_PRICE,
        RenderStepEnum.CART_SN,
        RenderStepEnum.DISTRIBUTION,
        RenderStepEnum.PLATFORM_COMMISSION
    };
}
