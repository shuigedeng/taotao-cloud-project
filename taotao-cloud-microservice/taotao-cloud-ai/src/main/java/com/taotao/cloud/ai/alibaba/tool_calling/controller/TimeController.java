package com.taotao.cloud.ai.alibaba.tool_calling.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/**
 * @author yingzi
 * @date 2025/5/21 11:01
 */

@RestController
@RequestMapping("/chat/time")
public class TimeController {

    private final ChatClient chatClient;
    private final InMemoryChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
    private final int MAX_MESSAGES = 100;
    private final MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository)
            .maxMessages(MAX_MESSAGES)
            .build();


    public TimeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                                .build()
                )
                .build();
    }

    /**
     * 无工具版
     */
    @GetMapping("/call")
    public String call(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        return chatClient.prompt(query).call().content();
    }

    /**
     * 调用工具版 - function
     */
    @GetMapping("/call/tool-function")
    public String callToolFunction(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        return chatClient.prompt(query).toolNames("getCityTimeFunction").call().content();
    }

    /**
     * 调用工具版 - method
     */
    @GetMapping("/call/tool-method")
    public String callToolMethod(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        return chatClient.prompt(query).tools(new TimeTools()).call().content();
    }

    /**
     * call 调用工具版 - method - false
     */
    @GetMapping("/call/tool-method-false")
    public ChatResponse callToolMethodFalse(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        ChatClient.CallResponseSpec call = chatClient.prompt(query).tools(new TimeTools())
                .advisors(
                        a -> a.param(CONVERSATION_ID, "yingzi")
                )
                .options(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false)  // 禁用内部工具执行
                        .build()
                )
                .call();
        return call.chatResponse();
    }

    @GetMapping("/messages")
    public List<Message> messages(@RequestParam(value = "conversation_id", defaultValue = "yingzi") String conversationId) {
        return messageWindowChatMemory.get(conversationId);
    }

}

