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

package com.taotao.cloud.report.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 店铺首页数据 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreIndexStatisticsVO {

    @Schema(description = "商品总数量")
    private Long goodsNum;

    @Schema(description = "订单总数量")
    private Integer orderNum;

    @Schema(description = "订单总额")
    private BigDecimal orderPrice;

    @Schema(description = "访客数UV")
    private Integer storeUV;

    @Schema(description = "待付款订单数量")
    private Long unPaidOrder;

    @Schema(description = "待发货订单数量")
    private Long unDeliveredOrder;

    @Schema(description = "待收货订单数量")
    private Long deliveredOrder;

    @Schema(description = "待处理退货数量")
    private Long returnGoods;

    @Schema(description = "待处理退款数量")
    private Long returnMoney;

    @Schema(description = "待回复评价数量")
    private Long memberEvaluation;

    @Schema(description = "待处理交易投诉数量")
    private Long complaint;

    @Schema(description = "待上架商品数量")
    private Long waitUpper;

    @Schema(description = "待审核商品数量")
    private Long waitAuth;

    @Schema(description = "可参与秒杀活动数量")
    private Long seckillNum;

    @Schema(description = "未对账结算单数量")
    private Long waitPayBill;
}
