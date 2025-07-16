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

package com.taotao.cloud.ai.springai.service;

import com.taotao.cloud.ai.springai.model.query.AiMessageQuery;
import org.springframework.ai.chat.messages.AssistantMessage;
import reactor.core.publisher.Flux;

/**
 * AiService
 */
public interface AiService {
    /**
     * ollama 回复
     *
     * @param messageQuery 提问消息
     * @return 返回消息
     */
    Flux<AssistantMessage> ollamaStream(AiMessageQuery messageQuery);

    /**
     * ollama 整合消息
     *
     * @param messageQuery 提问消息
     * @return 整合后的消息
     */
    Flux<AssistantMessage> ollamaConsolidateMessage(AiMessageQuery messageQuery);
}
