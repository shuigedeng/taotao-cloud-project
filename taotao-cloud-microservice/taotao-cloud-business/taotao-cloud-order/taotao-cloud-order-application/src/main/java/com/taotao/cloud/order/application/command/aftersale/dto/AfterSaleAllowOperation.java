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

package com.taotao.cloud.order.application.command.aftersale.dto;

import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleCO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/** 售后可操作类型 */
@RecordBuilder
@Schema(description = "售后可操作类型")
public record AfterSaleAllowOperation(
        @Schema(description = "可以确认售后") Boolean confirm,
        @Schema(description = "可以回寄物流") Boolean returnGoods,
        @Schema(description = "可以收货") Boolean rog,
        @Schema(description = "可以退款") Boolean refund,
        @Schema(description = "买家确认收货") Boolean buyerConfirm,
        @Schema(description = "可以取消") Boolean cancel)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    /** 根据各种状态构建对象 */
    public AfterSaleAllowOperation(AfterSaleCO afterSaleCO) {
        this(
                afterSaleCO.serviceStatus().equals(AfterSaleStatusEnum.APPLY.name()),
                false,
                afterSaleCO.serviceStatus().equals(AfterSaleStatusEnum.BUYER_RETURN.name()),
                afterSaleCO.serviceStatus().equals(AfterSaleStatusEnum.WAIT_REFUND.name()),
                false,
                afterSaleCO.serviceStatus().equals(AfterSaleStatusEnum.APPLY.name()));
    }
}
