//package com.taotao.cloud.ai.api;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AIController {
//
//	@Resource
//	private ChatClient ollamaChatClient;
//
//	@GetMapping("/chat")
//	public String chat(@RequestParam(name = "message") String message) {
//		return ollamaChatClient(message);
//	}
//
//}
//
