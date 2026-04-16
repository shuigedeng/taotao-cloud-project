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

package com.taotao.cloud.ai.springai.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.Getter;

/**
 * 消息对应角色枚举
 *
 */
@AllArgsConstructor
@Getter
public enum MessageRoleEnum {
    USER("user", "用户发送的消息"),
    /**
     * <a href="https://docs.spring.io/spring-ai/reference/api/chat/functions/openai-chat-functions.html">...</a>
     */
    FUNCTION("function", "自定义返回"),
    SYSTEM("system", "系统发送的消息"),
    ASSISTANT("assistant", "AI回复的消息");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final String value;

    private final String label;
}
