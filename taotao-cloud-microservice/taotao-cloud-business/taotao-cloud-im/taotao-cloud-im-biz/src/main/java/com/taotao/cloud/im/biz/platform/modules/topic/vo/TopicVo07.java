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

package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.modules.topic.enums.TopicReplyTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo07 {

    /** 回复id */
    @NotNull(message = "回复id不能为空")
    private Long replyId;

    /** 回复类型 */
    @NotNull(message = "回复类型不能为空")
    private TopicReplyTypeEnum replyType;

    /** 内容 */
    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容长度不能大于2000")
    private String content;
}
