package com.taotao.cloud.ai.spring;

import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class ChatController {

	private final ChatClient chatClient;

	@Value("classpath:your-prompt-template.st")
	Resource promptTemplateResource;

	@Autowired
	public ChatController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/chatStream")
	public Flux<String> chatSteam(@RequestParam String input) {

		PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);
		Prompt prompt = promptTemplate.create(Map.of("input", input));
		return chatClient.prompt(prompt).stream().content();

		//PromptTemplate promptTemplate = new PromptTemplate("我想知道{company}的最新财务状况");
		//DashscopeChatOptionsBuilder opsBuilder = DashScopeChatOptions.builder()
		//        .withFunction("xueQiuFinanceFunction");
		//DashScopeChatOptions ops = opsBuilder.build();
		//Map<String, Object> map = Map.of("company", input);
		//Prompt promp = promptTemplate.create(map, ops);
		//return chatClient.prompt(promp).stream().content();
	}
}
