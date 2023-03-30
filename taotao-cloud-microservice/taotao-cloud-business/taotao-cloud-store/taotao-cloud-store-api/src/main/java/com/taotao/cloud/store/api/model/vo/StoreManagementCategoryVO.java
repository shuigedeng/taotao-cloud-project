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

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 店铺经营范围 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺经营范围")
public class StoreManagementCategoryVO {

    @Schema(description = "已选择")
    private Boolean selected;

    /** 分类名称 */
    private String name;

    /** 父id, 根节点为0 */
    private Long parentId;

    /** 层级, 从0开始 */
    private Integer level;

    /** 排序值 */
    private Integer sortOrder;

    /** 佣金比例 */
    private BigDecimal commissionRate;

    /** 分类图标 */
    private String image;

    /** 是否支持频道 */
    private Boolean supportChannel;
}
