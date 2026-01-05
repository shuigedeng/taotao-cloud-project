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

package com.taotao.cloud.order.biz.service.business.trade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.sys.model.page.order.OrderLogPageQuery;
import com.taotao.cloud.order.biz.mapper.trade.OrderLogMapper;
import com.taotao.cloud.order.biz.model.entity.order.OrderLog;
import com.taotao.cloud.order.biz.service.business.trade.OrderLogService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 订单日志业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:53
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {

    @Override
    public List<OrderLog> getOrderLog(String orderSn) {
        LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderLog::getOrderSn, orderSn);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public IPage<OrderLog> pageQuery(OrderLogPageQuery orderLogPageQuery) {
        LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
        // todo 需要设置条件

        return this.page(orderLogPageQuery.buildMpPage(), lambdaQueryWrapper);
    }
}
