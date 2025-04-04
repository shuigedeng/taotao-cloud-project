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

package com.taotao.cloud.promotion.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.*;
import lombok.experimental.SuperBuilder;

/** 砍价活动参与实体类 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "砍价活动参与记录对象")
public class KanjiaActivityDTO extends KanjiaActivityLogDTO {

    @Schema(description = "砍价商品Id")
    private Long kanjiaActivityGoodsId;
}
