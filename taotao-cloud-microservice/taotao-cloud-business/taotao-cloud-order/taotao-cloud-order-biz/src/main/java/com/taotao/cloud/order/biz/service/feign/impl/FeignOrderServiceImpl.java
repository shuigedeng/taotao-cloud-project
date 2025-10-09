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

package com.taotao.cloud.order.biz.service.feign.impl;

import com.taotao.cloud.order.biz.mapper.order.IOrderMapper;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.repository.order.OrderRepository;
import com.taotao.cloud.order.biz.repository.order.IOrderRepository;
import com.taotao.cloud.order.biz.service.business.order.IOrderService;
import com.taotao.cloud.order.biz.service.feign.IFeignOrderService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DictServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:26:36
 */
@Service
@AllArgsConstructor
public class FeignOrderServiceImpl
        extends BaseSuperServiceImpl<Order, Long,IOrderMapper,  OrderRepository, IOrderRepository>
        implements IFeignOrderService {

    @Autowired
    private IOrderService orderService;

    @Override
    public <T> T test123(T t) {
        return t;
    }
}
