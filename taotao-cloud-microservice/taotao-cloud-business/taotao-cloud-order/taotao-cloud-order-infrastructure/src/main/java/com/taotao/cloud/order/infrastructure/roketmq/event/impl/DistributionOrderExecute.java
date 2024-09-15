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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.cloud.distribution.api.enums.DistributionOrderStatusEnum;
import com.taotao.cloud.distribution.api.feign.IFeignDistributionOrderApi;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.infrastructure.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.infrastructure.roketmq.event.AfterSaleStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.boot.web.timetask.EveryDayExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销订单入库
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:34:50
 */
@Service
public class DistributionOrderExecute implements OrderStatusChangeEvent, EveryDayExecute, AfterSaleStatusChangeEvent {

    /** 分销订单 */
    @Autowired
    private IFeignDistributionOrderApi distributionOrderService;

    @Override
    public void orderChange(OrderMessage orderMessage) {
        switch (orderMessage.newStatus()) {
                // 订单带校验/订单代发货，则记录分销信息
            case TAKE, UNDELIVERED -> {
                // 记录分销订单
                distributionOrderService.calculationDistribution(orderMessage.orderSn());
            }
            case CANCELLED -> {
                // 修改分销订单状态
                distributionOrderService.cancelOrder(orderMessage.orderSn());
            }
            default -> {}
        }
    }

    @Override
    public void execute() {
        // 计算分销提佣
        distributionOrderService.rebate(DistributionOrderStatusEnum.WAIT_BILL.name(), new DateTime());
        // 修改分销订单状态
        distributionOrderService.updateStatus();
    }

    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            distributionOrderService.refundOrder(afterSale.getSn());
        }
    }
}
