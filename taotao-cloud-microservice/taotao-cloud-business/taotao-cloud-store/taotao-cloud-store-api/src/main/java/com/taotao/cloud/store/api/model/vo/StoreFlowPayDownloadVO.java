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

package com.taotao.cloud.store.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

/** 店铺流水下载 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺流水下载")
public class StoreFlowPayDownloadVO {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    @Schema(description = "订单sn")
    private String orderSn;

    @Schema(description = "店铺名称 ")
    private String storeName;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "销售量")
    private Integer num;

    @Schema(description = "流水金额")
    private BigDecimal finalPrice;

    @Schema(description = "平台收取交易佣金")
    private BigDecimal commissionPrice;

    @Schema(description = "平台优惠券 使用金额")
    private BigDecimal siteCouponPrice;

    @Schema(description = "单品分销返现支出")
    private BigDecimal distributionRebate;

    @Schema(description = "积分活动商品结算价格")
    private BigDecimal pointSettlementPrice;

    @Schema(description = "砍价活动商品结算价格")
    private BigDecimal kanjiaSettlementPrice;

    @Schema(description = "最终结算金额")
    private BigDecimal billPrice;
}
