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

package com.taotao.cloud.promotion.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 积分商品实体类 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsBaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    private Long id;

    @Schema(description = "商品编号")
    private Long goodsId;

    @Schema(description = "商品sku编号")
    private Long skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品原价")
    private BigDecimal originalPrice;

    @Schema(description = "结算价格")
    private BigDecimal settlementPrice;

    @Schema(description = "积分商品分类编号")
    private String pointsGoodsCategoryId;

    @Schema(description = "分类名称")
    private String pointsGoodsCategoryName;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "活动库存数量")
    private Integer activeStock;

    @Schema(description = "兑换积分")
    private Long points;
}
