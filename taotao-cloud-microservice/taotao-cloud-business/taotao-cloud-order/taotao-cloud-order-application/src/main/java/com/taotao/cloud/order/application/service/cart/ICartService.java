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

package com.taotao.cloud.order.application.service.cart;

import com.taotao.cloud.order.application.command.order.dto.clientobject.ReceiptCO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.TradePO;

/**
 * 购物车业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:41
 */
public interface ICartService {

	/**
	 * 获取整笔交易
	 *
	 * @param checkedWay 购物车类型
	 * @return {@link TradeDTO }
	 * @since 2022-04-28 08:49:41
	 */
	TradeDTO readDTO(CartTypeEnum checkedWay);

	/**
	 * 获取整个交易中勾选的购物车和商品
	 *
	 * @param way 获取方式
	 * @return {@link TradeDTO }
	 * @since 2022-04-28 08:49:41
	 */
	TradeDTO getCheckedTradeDTO(CartTypeEnum way);

	/**
	 * 获取可使用的优惠券数量
	 *
	 * @param checkedWay 购物车类型 购物车购买：CART/立即购买：BUY_NOW/拼团购买：PINTUAN / 积分购买：POINT
	 * @return {@link Long }
	 * @since 2022-04-28 08:49:41
	 */
	Long getCanUseCoupon(CartTypeEnum checkedWay);

	/**
	 * 获取整个交易中勾选的购物车和商品
	 *
	 * @return {@link TradeDTO }
	 * @since 2022-04-28 08:49:41
	 */
	TradeDTO getAllTradeDTO();

	/**
	 * 购物车加入一个商品
	 *
	 * @param skuId    要写入的skuId
	 * @param num      要加入购物车的数量
	 * @param cartType 购物车类型
	 * @param cover    是否覆盖购物车的数量，如果为否则累加，否则直接覆盖
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:39:23
	 */
	boolean add(String skuId, Integer num, String cartType, Boolean cover);

	/**
	 * 更新选中状态
	 *
	 * @param skuId   要写入的skuId
	 * @param checked 是否选中
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:40:36
	 */
	boolean checked(String skuId, boolean checked);

	/**
	 * 更新某个店铺的所有商品的选中状态
	 *
	 * @param storeId 店铺Id
	 * @param checked 是否选中
	 * @return {@link Boolean }
	 * @since 2023-07-04 09:27:02
	 */
	boolean checkedStore(String storeId, boolean checked);

	/**
	 * 更新全部的选中状态
	 *
	 * @param checked 是否选中
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:41:20
	 */
	boolean checkedAll(boolean checked);

	/**
	 * 批量删除
	 *
	 * @param skuIds 要写入的skuIds
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:41:42
	 */
	boolean delete(String[] skuIds);

	/**
	 * 清空购物车
	 *
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:42:00
	 */
	boolean clean();

	/**
	 * 清空购物车无效数据
	 *
	 * @param way 购物车类型
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:42:20
	 */
	boolean cleanChecked(CartTypeEnum way);

	/**
	 * 重新写入
	 *
	 * @param tradeDTO 购物车构建器最终要构建的成品
	 * @return {@link Boolean }
	 * @since 2023-07-04 09:27:09
	 */
	boolean resetTradeDTO(TradeDTO tradeDTO);

	/**
	 * 选择收货地址
	 *
	 * @param shippingAddressId 收货地址id
	 * @param way               购物车类型
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:44:18
	 */
	boolean shippingAddress(String shippingAddressId, String way);

	/**
	 * 选择发票
	 *
	 * @param receiptCO 发票信息
	 * @param way       购物车类型
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:44:02
	 */
	boolean shippingReceipt(ReceiptCO receiptCO, String way);

	/**
	 * 选择配送方式
	 *
	 * @param storeId        店铺id
	 * @param deliveryMethod 配送方式
	 * @param way            购物车类型
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:43:50
	 */
	boolean shippingMethod(String storeId, String deliveryMethod, String way);

	/**
	 * 获取购物车商品数量
	 *
	 * @param checked 是否选择
	 * @return {@link Long }
	 * @since 2022-04-28 08:49:41
	 */
	Long getCartNum(Boolean checked);

	/**
	 * 选择优惠券
	 *
	 * @param couponId 优惠券id
	 * @param way      购物车类型
	 * @param use      true使用 false 弃用
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:47:50
	 */
	boolean selectCoupon(String couponId, String way, boolean use);

	/**
	 * 创建交易 1.获取购物车类型，不同的购物车类型有不同的订单逻辑 购物车类型：购物车、立即购买、虚拟商品、拼团、积分 2.校验用户的收件人信息 3.设置交易的基础参数
	 * 4.交易信息存储到缓存中 5.创建交易 6.清除购物车选择数据
	 *
	 * @param tradeDTO 创建交易参数
	 * @return {@link TradePO }
	 * @since 2022-04-28 08:49:41
	 */
	TradePO createTrade(TradeDTO tradeDTO);
}
