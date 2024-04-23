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
import jakarta.validation.Valid;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商品关联参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:36:45
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品参数分组")
public class GoodsParamsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4892783539320159200L;

    @Schema(description = "分组id")
    private Long groupId;

    @Schema(description = "分组名称")
    private String groupName;

    @Valid
    @Schema(description = "分组内的商品参数列表")
    private List<GoodsParamsItemDTO> goodsParamsItemDTOList;
}
