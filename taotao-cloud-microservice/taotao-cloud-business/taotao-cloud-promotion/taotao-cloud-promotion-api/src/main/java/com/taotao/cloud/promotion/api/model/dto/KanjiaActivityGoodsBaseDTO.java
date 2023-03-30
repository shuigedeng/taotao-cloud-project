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

package com.taotao.cloud.promotion.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 砍价活动商品实体类 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsBaseDTO implements Serializable {

    @Schema(description = "结算价格")
    @NotEmpty(message = "结算价格不能为空")
    private BigDecimal settlementPrice;

    @Schema(description = "商品原价")
    private BigDecimal originalPrice;

    @Schema(description = "最低购买金额")
    @NotEmpty(message = "最低购买金额不能为空")
    private BigDecimal purchasePrice;

    @Schema(description = "货品id")
    @NotEmpty(message = "货品id不能为空")
    private Long goodsId;

    @Schema(description = "货品SkuId")
    @NotEmpty(message = "货品SkuId不能为空")
    private Long skuId;

    @Schema(description = "货品名称")
    private String goodsName;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "活动库存")
    @NotEmpty(message = "活动库存不能为空")
    private Integer stock;

    @Schema(description = "每人最低砍价金额")
    @NotEmpty(message = "每人最低砍价金额不能为空")
    private BigDecimal lowestPrice;

    @Schema(description = "每人最高砍价金额")
    @NotEmpty(message = "每人最高砍价金额不能为空")
    private BigDecimal highestPrice;
}
