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

package com.taotao.cloud.order.application.command.order.dto;

import com.taotao.boot.common.model.PageQuery;
import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 订单投诉查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单投诉查询参数")
public class OrderComplaintPageQry extends PageQuery {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    /**
     * @see ComplaintStatusEnum
     */
    @Schema(description = "交易投诉状态")
    private String status;

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "会员id")
    private Long memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "商家id")
    private Long storeId;

    @Schema(description = "商家名称")
    private String storeName;
}
