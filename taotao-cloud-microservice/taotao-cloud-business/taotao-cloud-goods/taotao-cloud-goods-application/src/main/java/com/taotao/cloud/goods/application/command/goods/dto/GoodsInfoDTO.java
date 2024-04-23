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

package com.taotao.cloud.goods.application.command.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 微信小程序直播商品DTO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoDTO {

    @Schema(description = "图片mediaID")
    private String coverImgUrl;

    @Schema(description = "商品名称")
    private String name;

    /**
     * 1：一口价（只需要传入price，price2不传）
     *
     * <p>2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
     *
     * <p>3：显示折扣价（price字段为原价，price2字段为现价 price和price2必传
     */
    @Schema(description = "价格类型")
    private Integer priceType;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "价格2")
    private BigDecimal price2;

    @Schema(description = "商品详情页的小程序路径")
    private String url;

    // public GoodsInfo(Commodity commodity) {
    //	BeanUtil.copyProperties(commodity, this);
    // }
}
