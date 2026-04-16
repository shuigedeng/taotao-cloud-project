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

package com.taotao.cloud.ai.springai.constants;

/**
 * 系统提问AI常量
 *
 */
public interface ChatRequestConstant {

    String SYSTEM_SIMPLIFICATION = "简要总结一下对话内容，用作后续的上下文提示 prompt，控制在 200 字以内";

    String SYSTEM_HISTORICAL_MESSAGES = "这是历史聊天总结作为前情提要: %s";
}
