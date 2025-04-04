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
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/** 积分商品分类视图对象 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5528833118735059182L;

    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "父id, 根节点为0")
    private Long parentId;

    @Schema(description = "层级, 从0开始")
    private Integer level;

    @Schema(description = "排序值")
    private BigDecimal sortOrder;
}
