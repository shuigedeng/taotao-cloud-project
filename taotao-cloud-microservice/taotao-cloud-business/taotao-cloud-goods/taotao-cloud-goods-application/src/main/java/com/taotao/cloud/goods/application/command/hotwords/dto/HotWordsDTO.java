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

package com.taotao.cloud.goods.application.command.hotwords.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 搜索热词 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotWordsDTO {

    @NotBlank(message = "搜索热词不能为空")
    @Size(max = 20, min = 1, message = "搜索热词长度限制在1-20")
    private String keywords;

    @NotNull(message = "分数不能为空")
    @Max(value = 9999999999L, message = "分数不能大于9999999999")
    @Min(value = -9999999999L, message = "分数不能小于9999999999")
    private Integer point;
}
