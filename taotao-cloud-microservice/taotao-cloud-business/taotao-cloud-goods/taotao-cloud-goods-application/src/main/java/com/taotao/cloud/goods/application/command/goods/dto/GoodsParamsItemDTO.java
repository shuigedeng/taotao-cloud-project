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
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数项
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:36:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品参数列表")
public class GoodsParamsItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8823775607604091035L;

    @Schema(description = "参数ID")
    private Long paramId;

    @Schema(description = "参数名字")
    private String paramName;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "是否可索引，0 不索引 1 索引")
    private Integer isIndex;

    @Schema(description = "是否必填，0 不显示 1 显示")
    private Integer required;

    @Schema(description = "排序")
    private Integer sort;
}
