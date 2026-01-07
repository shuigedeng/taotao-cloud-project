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

package com.taotao.cloud.distribution.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.distribution.api.model.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;

/** 分销订单业务层 */
public interface DistributionOrderService extends IService<DistributionOrder> {

    /**
     * 获取分销订单分页
     *
     * @param distributionOrderPageQuery 分销订单搜索参数
     * @return 分销订单分页
     */
    IPage<DistributionOrder> getDistributionOrderPage(DistributionOrderPageQuery distributionOrderPageQuery);

    /**
     * 支付订单 记录分销订单
     *
     * @param orderSn 订单编号
     */
    void calculationDistribution(String orderSn);

    /**
     * 取消订单 记录分销订单
     *
     * @param orderSn 订单编号
     */
    void cancelOrder(String orderSn);

    /**
     * 订单退款 记录分销订单
     *
     * @param afterSaleSn 售后单号
     */
    void refundOrder(String afterSaleSn);
}
