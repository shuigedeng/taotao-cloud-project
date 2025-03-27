package com.taotao.cloud.ai.mcp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatbotService {
	@Autowired
	private ChatClient chatClient;

	String chat(String question) {
		return chatClient
			.prompt()
			.user(question)
			.call()
			.content();
	}

}
