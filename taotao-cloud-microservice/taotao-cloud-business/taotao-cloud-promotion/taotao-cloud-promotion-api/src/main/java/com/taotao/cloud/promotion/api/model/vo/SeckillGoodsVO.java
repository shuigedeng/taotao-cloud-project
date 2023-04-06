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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 秒杀活动商品视图对象 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoodsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5170316685407828228L;

    @Schema(description = "活动id")
    private Long seckillId;

    @Schema(description = "时刻")
    private Integer timeLine;

    @Schema(description = "商品id")
    private Long goodsId;

    @Schema(description = "以积分渠道购买需要积分数量")
    private Integer point;

    @Schema(description = "skuID")
    private Long skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品图片")
    private String goodsImage;

    @Schema(description = "商家id")
    private Long storeId;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "促销数量")
    private Integer quantity;

    @Schema(description = "已售数量")
    private Integer salesNum;

    @Schema(description = "商品原始价格")
    private BigDecimal originalPrice;
}
