package com.taotao.cloud.ai.mcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ChatbotController {
	@Autowired
	private ChatbotService chatbotService;

	@PostMapping("/chat")
	ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
		String answer = chatbotService.chat(chatRequest.question());
		return ResponseEntity.ok(new ChatResponse(answer));
	}

	record ChatRequest(String question) {
	}

	record ChatResponse(String answer) {
	}

}
