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

package com.taotao.cloud.ai.alibaba.chat.openai_chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/5/24 16:52
 */
@RestController
@RequestMapping("/chat/option")
public class ChatOptionController {

    private final ChatClient chatClient;

    public ChatOptionController(ChatClient.Builder builder) {
        this.chatClient =
                builder.defaultOptions(OpenAiChatOptions.builder().temperature(0.9).build())
                        .build();
    }

    @GetMapping("/call")
    public String call(
            @RequestParam(value = "query", defaultValue = "你好，请为我创造一首以“影子”为主题的诗") String query) {
        return chatClient.prompt(query).call().content();
    }

    @GetMapping("/call/temperature")
    public String callOption(
            @RequestParam(value = "query", defaultValue = "你好，请为我创造一首以“影子”为主题的诗") String query) {
        return chatClient
                .prompt(query)
                .options(OpenAiChatOptions.builder().temperature(0.0).build())
                .call()
                .content();
    }
}
