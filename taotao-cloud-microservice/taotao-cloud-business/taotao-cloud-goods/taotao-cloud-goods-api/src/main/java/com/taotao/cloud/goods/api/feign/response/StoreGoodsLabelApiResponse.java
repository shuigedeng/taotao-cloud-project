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
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 店铺分类VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:23
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreGoodsLabelApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "店铺商品分类ID")
    private Long id;

    @Schema(description = "店铺商品分类名称")
    private String labelName;

    @Schema(description = "层级, 从0开始")
    private Integer level;

    @Schema(description = "店铺商品分类排序")
    private Integer sortOrder;

    @Schema(description = "下级分类列表")
    private List<StoreGoodsLabelApiResponse> children;

    public StoreGoodsLabelApiResponse(Long id, String labelName, Integer level, Integer sortOrder) {
        this.id = id;
        this.labelName = labelName;
        this.level = level;
        this.sortOrder = sortOrder;
    }
}
