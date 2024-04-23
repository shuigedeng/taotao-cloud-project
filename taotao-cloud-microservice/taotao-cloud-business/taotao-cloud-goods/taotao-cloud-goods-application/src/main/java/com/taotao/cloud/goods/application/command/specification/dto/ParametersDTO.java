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

package com.taotao.cloud.goods.application.command.specification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 商品参数 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametersDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 724427321881170297L;

    @Schema(description = "参数名称")
    private String paramName;

    @Schema(description = "选择值")
    private String options;

    @Schema(description = "是否可索引，0 不显示 1 显示")
    private Integer isIndex;

    @Schema(description = "是否必填 是1否0")
    private Integer required;

    @Schema(description = "参数分组id")
    private Long groupId;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "排序")
    private Integer sort;
}
