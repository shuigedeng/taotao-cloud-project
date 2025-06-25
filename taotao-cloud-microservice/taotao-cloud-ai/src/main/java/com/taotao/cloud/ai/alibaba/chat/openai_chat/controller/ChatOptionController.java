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
        this.chatClient = builder
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .temperature(0.9)
                                .build()
                )
                .build();
    }

    @GetMapping("/call")
    public String call(@RequestParam(value = "query", defaultValue = "你好，请为我创造一首以“影子”为主题的诗")String query) {
        return chatClient.prompt(query).call().content();
    }

    @GetMapping("/call/temperature")
    public String callOption(@RequestParam(value = "query", defaultValue = "你好，请为我创造一首以“影子”为主题的诗")String query) {
        return chatClient.prompt(query)
                .options(
                        OpenAiChatOptions.builder()
                                .temperature(0.0)
                                .build()
                )
                .call().content();
    }
}
