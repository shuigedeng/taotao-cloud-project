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

package com.taotao.cloud.order.application.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderItemPO;
import java.util.List;

/**
 * 子订单业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:41
 */
public interface IOrderItemService extends IService<OrderItemPO> {

	/**
	 * 更新评论状态
	 *
	 * @param orderItemSn       子订单编号
	 * @param commentStatusEnum 评论状态枚举
	 * @return {@link Boolean }
	 * @since 2022-05-16 17:23:40
	 */
	Boolean updateCommentStatus(String orderItemSn, CommentStatusEnum commentStatusEnum);

	/**
	 * 更新可申请售后状态
	 *
	 * @param orderItemSn                  子订单编号
	 * @param orderItemAfterSaleStatusEnum 售后状态枚举
	 * @return {@link Boolean }
	 * @since 2022-05-16 17:23:39
	 */
	Boolean updateAfterSaleStatus(String orderItemSn,
		OrderItemAfterSaleStatusEnum orderItemAfterSaleStatusEnum);

	/**
	 * 更新订单可投诉状态
	 *
	 * @param orderSn            订单sn
	 * @param skuId              商品skuId
	 * @param complainId         订单交易投诉ID
	 * @param complainStatusEnum 修改状态
	 * @return {@link Boolean }
	 * @since 2022-05-16 17:23:36
	 */
	Boolean updateOrderItemsComplainStatus(
		String orderSn, Long skuId, Long complainId, OrderComplaintStatusEnum complainStatusEnum);

	/**
	 * 根据子订单编号获取子订单信息
	 *
	 * @param sn 子订单编号
	 * @return {@link OrderItemPO }
	 * @since 2022-04-28 08:54:41
	 */
	OrderItemPO getBySn(String sn);

	/**
	 * 根据订单编号获取子订单列表
	 *
	 * @param orderSn 订单编号
	 * @return {@link List }<{@link OrderItemPO }>
	 * @since 2022-04-28 08:54:41
	 */
	List<OrderItemPO> getByOrderSn(String orderSn);

	/**
	 * 子订单查询
	 *
	 * @param orderSn 订单编号
	 * @param skuId   skuid
	 * @return {@link OrderItemPO }
	 * @since 2022-04-28 08:54:41
	 */
	OrderItemPO getByOrderSnAndSkuId(String orderSn, Long skuId);
}
