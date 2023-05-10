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
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 砍价商品视图对象 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsListVO implements Serializable {

    @Schema(description = "砍价活动商品id")
    private String id;

    @Schema(description = "货品名称")
    private String goodsName;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "最低购买金额")
    private BigDecimal purchasePrice;

    @Schema(description = "活动库存")
    private Integer stock;
}
