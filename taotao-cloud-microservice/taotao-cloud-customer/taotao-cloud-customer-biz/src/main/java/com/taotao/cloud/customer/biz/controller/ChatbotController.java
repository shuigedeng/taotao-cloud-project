/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.customer.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.customer.api.vo.ChatbotVO;
import com.taotao.cloud.customer.biz.entity.Chatbot;
import com.taotao.cloud.customer.biz.mapper.ChatbotMapper;
import com.taotao.cloud.customer.biz.service.IChatbotService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机器人客服管理API
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/13 09:58
 */
@Validated
@RestController
@RequestMapping("/chatbot")
@Tag(name = "机器人客服管理API", description = "机器人客服管理API")
public class ChatbotController {

	private final IChatbotService chatbotService;

	public ChatbotController(IChatbotService chatbotService) {
		this.chatbotService = chatbotService;
	}

	@Operation(summary = "根据id查询机器人客服信息", description = "根据id查询机器人客服信息")
	@RequestLogger("根据id查询机器人客服信息")
	@PreAuthorize("hasAuthority('chatbot:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<ChatbotVO> findChatbotById(@PathVariable(value = "id") Long id) {
		Chatbot chatbot = chatbotService.findChatbotById(id);
		ChatbotVO vo = ChatbotMapper.INSTANCE.chatbotToChatbotVO(chatbot);
		return Result.success(vo);
	}

}
