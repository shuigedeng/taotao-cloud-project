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

package com.taotao.cloud.order.api.feign.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@Schema(description = "订单添加对象")
public record OrderSaveApiRequest(

        /** 买家ID */
        @Schema(description = "买家ID") Long memberId,
        /** 订单编码 */
        @Schema(description = "订单编码") String code,
        /** 订单金额 */
        @Schema(description = "订单金额") BigDecimal amount,
        /** 订单主状态 */
        @Schema(description = "订单主状态") Integer mainStatus,
        /** 订单子状态 */
        @Schema(description = "订单子状态") Integer childStatus,
        /** 收货人姓名 */
        @Schema(description = "收货人姓名") String receiverName,
        /** 收货人电话 */
        @Schema(description = "收货人电话") String receiverPhone,
        /** 收货地址:json的形式存储 */
        @Schema(description = "收货地址:json的形式存储") String receiverAddressJson)
        implements Serializable {

    static final long serialVersionUID = 5126530068827085130L;
}
