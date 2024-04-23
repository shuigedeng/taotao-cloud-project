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

package com.taotao.cloud.member.application.command.evaluation.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 评价数量VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评价数量VO")
public class EvaluationNumberCO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "全部商品")
    private Integer all;

    @Schema(description = "好评数量")
    private Integer good;

    @Schema(description = "中评数量")
    private Integer moderate;

    @Schema(description = "差评数量")
    private Integer worse;

    @Schema(description = "有图数量")
    private Long haveImage;
}
