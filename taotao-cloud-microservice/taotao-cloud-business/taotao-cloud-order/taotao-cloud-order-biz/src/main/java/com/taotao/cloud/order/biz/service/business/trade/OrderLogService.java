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

package com.taotao.cloud.order.biz.service.business.trade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.sys.model.page.order.OrderLogPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.OrderLog;
import java.util.List;

/**
 * 订单日志业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:50
 */
public interface OrderLogService extends IService<OrderLog> {

    /**
     * 根据订单编号获取订单日志列表
     *
     * @param orderSn 订单编号
     * @return {@link List }<{@link OrderLog }>
     * @since 2022-04-28 08:55:50
     */
    List<OrderLog> getOrderLog(String orderSn);

    IPage<OrderLog> pageQuery(OrderLogPageQuery orderLogPageQuery);
}
