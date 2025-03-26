package com.taotao.cloud.ai.mcp;

import org.springframework.ai.chat.client.ChatClient;

public class ChatbotService {
	@Auto
	private ChatClient chatClient;

	String chat(String question) {
		return chatClient
			.prompt()
			.user(question)
			.call()
			.content();
	}

}
