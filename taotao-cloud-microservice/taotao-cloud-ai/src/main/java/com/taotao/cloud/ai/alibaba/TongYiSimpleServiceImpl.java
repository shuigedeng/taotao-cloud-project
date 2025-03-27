//package com.taotao.cloud.ai.alibaba;
//
//import com.taotao.cloud.ai.tongyi.service.AbstractTongYiServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class TongYiSimpleServiceImpl extends AbstractTongYiServiceImpl {
//
//	/**
//	 * 自动注入ChatClient、StreamingChatClient，屏蔽模型调用细节
//	 */
//	private final ChatClient chatClient;
//
//	private final StreamingChatClient streamingChatClient;
//
//	@Autowired
//	public TongYiSimpleServiceImpl(ChatClient chatClient, StreamingChatClient streamingChatClient) {
//		this.chatClient = chatClient;
//		this.streamingChatClient = streamingChatClient;
//	}
//
//	/**
//	 * 具体实现：
//	 */
//	@Override
//	public String completion(String message) {
//		Prompt prompt = new Prompt(new UserMessage(message));
//		return chatClient.call(prompt).getResult().getOutput().getContent();
//	}
//}
