package com.taotao.cloud.ai.mcp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;

public class McpConfig {
	@Bean
	ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider toolCallbackProvider) {
		return ChatClient
			.builder(chatModel)
			.defaultTools(toolCallbackProvider.getToolCallbacks())
			.build();
	}


}
