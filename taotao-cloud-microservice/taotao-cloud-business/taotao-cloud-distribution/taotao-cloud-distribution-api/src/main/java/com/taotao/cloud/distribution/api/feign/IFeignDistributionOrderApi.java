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

package com.taotao.cloud.distribution.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.cloud.distribution.api.feign.fallback.FeignDistributionOrderServiceFallback;
import org.springframework.web.service.annotation.HttpExchange;

import java.time.LocalDateTime;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@HttpExchange(
        contextId = "IFeignDistributionOrderService",
        value = ServiceNameConstants.TAOTAO_CLOUD_GOODS,
        fallbackFactory = FeignDistributionOrderServiceFallback.class)
public interface IFeignDistributionOrderApi {

    // 记录分销订单
    void calculationDistribution(String orderSn);

    // 修改分销订单状态
    void cancelOrder(String orderSn);

    // 计算分销提佣
    void rebate(String name, LocalDateTime dateTime);

    /**
     * new LambdaUpdateWrapper<DistributionOrder>()
     * .eq(DistributionOrder::getDistributionOrderStatus,
     * DistributionOrderStatusEnum.WAIT_BILL.name()) .le(DistributionOrder::getSettleCycle, new
     * DateTime()) .set(DistributionOrder::getDistributionOrderStatus,
     * DistributionOrderStatusEnum.WAIT_CASH.name())
     */
    void updateStatus();

    void refundOrder(String sn);
}
