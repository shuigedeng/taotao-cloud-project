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

package com.taotao.cloud.auth.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 分销商品信息 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销商品信息")
public class DistributionGoodsVO {

    @Schema(description = "分销商品ID")
    private String id;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "规格")
    private String specs;

    @Schema(description = "库存")
    private Integer quantity;

    @Schema(description = "商品图片")
    private String thumbnail;

    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "商品编号")
    private String sn;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "规格ID")
    private String skuId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "佣金金额")
    private BigDecimal commission;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;
}
