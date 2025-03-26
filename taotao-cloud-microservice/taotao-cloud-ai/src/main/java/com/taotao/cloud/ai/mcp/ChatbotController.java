package com.taotao.cloud.ai.mcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class ChatbotController {
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
