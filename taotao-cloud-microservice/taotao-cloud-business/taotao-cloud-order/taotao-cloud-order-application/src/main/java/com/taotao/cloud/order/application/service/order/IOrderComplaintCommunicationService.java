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
import com.taotao.cloud.order.application.command.order.dto.OrderComplaintCommunicationPageQry;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderComplaintCommunicationPO;

/**
 * 订单投诉通信业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:33
 */
public interface IOrderComplaintCommunicationService extends IService<OrderComplaintCommunicationPO> {

	/**
	 * 添加订单投诉通信
	 *
	 * @param orderComplaintCommunicationPO 订单投诉通信
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:54:33
	 */
	Boolean addCommunication(OrderComplaintCommunicationPO orderComplaintCommunicationPO);

	/**
	 * 获取通信记录
	 *
	 * @param orderComplaintCommunicationPageQry 参数
	 * @return {@link IPage }<{@link OrderComplaintCommunicationPO }>
	 * @since 2022-04-28 08:54:33
	 */
	IPage<OrderComplaintCommunicationPO> getCommunication(
		OrderComplaintCommunicationPageQry orderComplaintCommunicationPageQry);
}
