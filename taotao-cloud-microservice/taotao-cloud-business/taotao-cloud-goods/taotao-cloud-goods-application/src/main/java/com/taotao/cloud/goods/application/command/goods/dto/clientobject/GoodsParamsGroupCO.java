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

package com.taotao.cloud.goods.application.command.goods.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 商品参数vo */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParamsGroupCO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1450550797436233753L;

    @Schema(description = "参数组关联的参数集合")
    private List<GoodsParamsCO> params;

    @Schema(description = "参数组名称")
    private String groupName;

    @Schema(description = "参数组id")
    private String groupId;
}