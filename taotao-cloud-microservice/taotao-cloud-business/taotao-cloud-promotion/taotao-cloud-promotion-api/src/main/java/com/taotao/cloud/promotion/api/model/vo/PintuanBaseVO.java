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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 拼团活动实体类 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanBaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    public static final String TABLE_NAME = "tt_pintuan";

    @Schema(description = "成团人数")
    private Integer requiredNum;

    @Schema(description = "限购数量")
    private Integer limitNum;

    @Schema(description = "虚拟成团", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean fictitious;

    @Schema(description = "拼团规则")
    private String pintuanRule;
}
