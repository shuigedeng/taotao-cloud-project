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

import com.taotao.boot.common.model.PageQuery;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 售后搜索参数
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
@Schema(description = "售后搜索参数")
public class AfterSalePageQry extends PageQuery {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    @Schema(description = "售后服务单号")
    private String sn;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "商家名称")
    private String storeName;

    @Schema(description = "商家ID")
    private Long storeId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "申请退款金额,可以为范围，如10_1000")
    private String applyRefundPrice;

    @Schema(description = "实际退款金额,可以为范围，如10_1000")
    private String actualRefundPrice;

    /**
     * @see AfterSaleTypeEnum
     */
    @Schema(description = "售后类型", allowableValues = "CANCEL,RETURN_GOODS,EXCHANGE_GOODS,REISSUE_GOODS")
    private String serviceType;

    /**
     * @see AfterSaleStatusEnum
     */
    @Schema(
            description = "售后单状态",
            allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
    private String serviceStatus;

    @Schema(description = "开始时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "结束时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
