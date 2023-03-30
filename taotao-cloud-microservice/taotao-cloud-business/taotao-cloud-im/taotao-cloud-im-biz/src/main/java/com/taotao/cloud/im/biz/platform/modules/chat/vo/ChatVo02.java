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

package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.modules.push.enums.PushMsgTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatVo02 {

    @NotNull(message = "群组不能为空")
    private Long groupId;

    @NotNull(message = "消息类型不能为空")
    private PushMsgTypeEnum msgType;

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 20000, message = "消息内容长度不能大于20000")
    private String content;
}
