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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.application.command.distribution.dto.DistributionPageQry;
import com.taotao.cloud.order.application.command.order.dto.StoreFlowPageQry;
import com.taotao.cloud.order.application.command.store.dto.StorePageQry;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSalePO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.StoreFlowPO;
import java.util.List;

/**
 * 商家订单流水业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:53
 */
public interface IStoreFlowService extends IService<StoreFlowPO> {

	/**
	 * 支付订单
	 *
	 * @param orderSn 订单编号
	 * @since 2022-04-28 08:54:53
	 */
	void payOrder(String orderSn);

	/**
	 * 订单退款
	 *
	 * @param afterSale 售后单
	 * @since 2022-04-28 08:54:53
	 */
	void refundOrder(AfterSalePO afterSale);

	/**
	 * 获取商家流水
	 *
	 * @param storeFlowQueryDTO 查询参数
	 * @return {@link IPage }<{@link StoreFlowPO }>
	 * @since 2022-04-28 08:54:53
	 */
	IPage<StoreFlowPO> getStoreFlow(StoreFlowPageQry storeFlowQueryDTO);

	/**
	 * 根据参数查询一条数据
	 *
	 * @param storeFlowQueryDTO 查询参数
	 * @return {@link StoreFlowPO }
	 * @since 2022-04-28 08:54:53
	 */
	StoreFlowPO queryOne(StoreFlowPageQry storeFlowQueryDTO);

	/**
	 * 获取结算单地入账流水
	 *
	 * @param storeFlowQueryDTO 查询条件
	 * @return {@link List }<{@link StoreFlowPayDownloadVO }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(StoreFlowPageQry storeFlowQueryDTO);

	/**
	 * 获取结算单的退款流水
	 *
	 * @param storeFlowQueryDTO 查询条件
	 * @return {@link List }<{@link StoreFlowRefundDownloadVO }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(
		StoreFlowPageQry storeFlowQueryDTO);

	/**
	 * 根据结算单ID获取商家流水
	 *
	 * @param storePageQry 存储页面查询
	 * @return {@link IPage }<{@link StoreFlowPO }>
	 * @since 2022-05-19 15:47:59
	 */
	IPage<StoreFlowPO> getStoreFlow(StorePageQry storePageQry);

	/**
	 * 根据结算单ID获取商家流水
	 *
	 * @param distributionPageQry 分配页面查询
	 * @return {@link IPage }<{@link StoreFlowPO }>
	 * @since 2022-05-19 15:48:02
	 */
	IPage<StoreFlowPO> getDistributionFlow(DistributionPageQry distributionPageQry);

	/**
	 * 获取店铺流水
	 *
	 * @param storeFlowQueryDTO 店铺流水查询参数
	 * @return {@link List }<{@link StoreFlowPO }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlowPO> listStoreFlow(StoreFlowPageQry storeFlowQueryDTO);
}
