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

package com.taotao.cloud.order.application.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.order.application.service.order.IOrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单自动取消（每分钟执行）
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:45
 */
@Component
public class CancelOrderTaskExecute implements EveryMinuteExecute {

    /** 订单 */
    @Autowired
    private IOrderService orderService;
    /** 设置 */
    @Autowired
    private IFeignSettingApi settingApi;

    @Autowired
    private DistributedLock distributedLock;

    @Override
    public void execute() {
        OrderSettingVO orderSetting = settingApi.getOrderSetting(SettingCategoryEnum.ORDER_SETTING.name());
        if (orderSetting != null && orderSetting.getAutoCancel() != null) {
            // 订单自动取消时间 = 当前时间 - 自动取消时间分钟数
            DateTime cancelTime = DateUtil.offsetMinute(DateUtil.date(), -orderSetting.getAutoCancel());
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Order::getOrderStatus, OrderStatusEnum.UNPAID.name());
            // 订单创建时间 <= 订单自动取消时间
            queryWrapper.le(Order::getCreateTime, cancelTime);
            List<Order> list = orderService.list(queryWrapper);
            List<String> cancelSnList = list.stream().map(Order::getSn).toList();
            for (String sn : cancelSnList) {
                orderService.systemCancel(sn, "超时未支付自动取消");
            }
        }
    }
}
