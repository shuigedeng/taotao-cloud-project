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

package com.taotao.cloud.payment.biz.bootx.core.aggregate.entity;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聚合支付发起信息
 *
 * @author xxm
 * @date 2022/3/5
 */
@Data
@Accessors(chain = true)
public class AggregatePayInfo {

    /** 标题 */
    private String title;

    /** 订单ID */
    private String businessId;

    /** 支付金额 */
    private BigDecimal amount;
}
