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

package com.taotao.cloud.goods.api.feign.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 商品分类 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品分类VO")
public class CategoryApiResponse {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = " 父id, 根节点为0")
    private Long parentId;

    @Schema(description = "层级, 从0开始")
    private Integer level;

    @Schema(description = "排序值")
    private Integer sortOrder;

    @Schema(description = "佣金比例")
    private BigDecimal commissionRate;

    @Schema(description = "分类图标")
    private String image;

    @Schema(description = "是否支持频道")
    private Boolean supportChannel;
}
