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

package com.taotao.cloud.ai.springai.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.*;
import org.hibernate.validator.constraints.Length;

/**
 * Ai消息请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "请求 AI 描述参数")
public class AiMessageQuery {

    @Schema(description = "消息集合")
    private List<ActualMessage> messages;

    @Schema(description = "模型")
    @Length(max = 60, message = "不存在改模型")
    private String model;

    /**
     * 较高的温度值会使得生成的文本更加随机和多样化，而较低的值会使得生成的文本更加确定和保守
     */
    @Schema(description = "话题新鲜度", example = "0.5F")
    @Max(value = 1, message = "话题新鲜度在 0 ~ 1 之间")
    private Double temperature;

    /**
     * 它控制了模型生成文本时考虑的词汇数量。较小的值会使生成的文本更加确定和重复，而较大的值会增加生成文本的多样性。
     */
    @Schema(description = "控制生成文本时的多样性( 目前未使用 )")
    @Max(value = 1, message = "生成文本时的多样性在 0 ~ 1 之间")
    private Integer topK;

    /**
     * 较小的值会导致生成的文本更加保守，而较大的值会增加生成文本的多样性
     */
    @Schema(description = "文章多样性", example = "0.5F")
    @Max(value = 1, message = "文本的多样性在 0 ~ 1 之间")
    private Double topP;

    /**
     * 它用于惩罚重复的单词或短语在生成文本中出现的频率。较大的值会增加惩罚，从而减少文本中的重复。
     */
    @Schema(description = "惩罚重复的强度", example = "0.5F")
    @Max(value = 1, message = "惩罚重复的强度 0 ~ 1 之间")
    private Double repeatPenalty;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "消息描述")
    public static class ActualMessage {

        @Schema(description = "角色")
        @Pattern(regexp = "^(user|system|assistant)$", message = "不存在该角色")
        private String role;

        @Schema(description = "内容")
        private String content;
    }
}
