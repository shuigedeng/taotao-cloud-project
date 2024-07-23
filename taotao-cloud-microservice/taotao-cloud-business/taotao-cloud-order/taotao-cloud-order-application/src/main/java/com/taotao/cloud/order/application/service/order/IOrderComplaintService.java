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
import com.taotao.cloud.order.application.command.order.dto.OrderComplaintAddCmd;
import com.taotao.cloud.order.application.command.order.dto.OrderComplaintOperationAddCmd;
import com.taotao.cloud.order.application.command.order.dto.OrderComplaintPageQry;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderComplaintCO;
import com.taotao.cloud.order.application.command.order.dto.StoreAppealCmd;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderComplaintPO;

/**
 * 交易投诉业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:36
 */
public interface IOrderComplaintService extends IService<OrderComplaintPO> {

    /**
     * 分页获取交易投诉信息
     *
     * @param orderComplaintPageQry 订单投诉页面查询
     * @return {@link IPage }<{@link OrderComplaintPO }>
     * @since 2022-04-28 08:54:36
     */
    IPage<OrderComplaintPO> pageQuery(OrderComplaintPageQry orderComplaintPageQry);

    /**
     * 获取交易投诉详情
     *
     * @param id 交易投诉ID
     * @return {@link OrderComplaintCO }
     * @since 2022-04-28 08:54:36
     */
    OrderComplaintCO getOrderComplainById(Long id);

    /**
     * 获取交易投诉详情
     *
     * @param storeId 店铺id
     * @return {@link OrderComplaintPO }
     * @since 2022-04-28 08:54:36
     */
    OrderComplaintPO getOrderComplainByStoreId(Long storeId);

    /**
     * 添加交易投诉
     *
     * @param orderComplaintAddCmd 交易投诉信息
     * @return {@link OrderComplaintPO }
     * @since 2022-04-28 08:54:36
     */
    OrderComplaintPO addOrderComplain(OrderComplaintAddCmd orderComplaintAddCmd);

    /**
     * 更新交易投诉
     *
     * @param orderComplaintPO 交易投诉信息
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:36
     */
    Boolean updateOrderComplain(OrderComplaintPO orderComplaintPO);

    /**
     * 修改交易投诉状态
     *
     * @param orderComplaintOperationAddCmd 订单投诉操作dto
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:36
     */
    Boolean updateOrderComplainByStatus(OrderComplaintOperationAddCmd orderComplaintOperationAddCmd);

    /**
     * 待处理投诉数量
     *
     * @return long
     * @since 2022-04-28 08:54:37
     */
    long waitComplainNum();

    /**
     * 取消交易投诉
     *
     * @param id 交易投诉ID
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:37
     */
    Boolean cancel(Long id);

    /**
     * 店铺申诉
     *
     * @param storeAppealDTO 商店吸引力dto
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:37
     */
    Boolean appeal(StoreAppealCmd storeAppealDTO);
}
