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

import java.math.BigDecimal;

/**
 * 订单价格
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:44
 */
public interface IOrderPriceService {

    /**
     * 价格修改 日志功能内部实现
     *
     * @param orderSn 订单编号
     * @param orderPrice 订单价格
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:44
     */
    Boolean updatePrice(String orderSn, BigDecimal orderPrice);

    /**
     * 管理员订单付款
     *
     * @param orderSn 订单编号
     * @return {@link Boolean }
     * @since 2022-05-16 16:58:31
     */
    Boolean adminPayOrder(String orderSn);
}
