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

package com.taotao.cloud.goods.application.command.specification.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 规格项表规格项 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "参数组关联的参数集合")
public class SpecificationCO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4433579132929428572L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "所属卖家 0属于平台")
    private Long storeId;

    @Schema(description = "规格值名字, 《,》分割")
    private String specValue;
}
