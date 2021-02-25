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
package com.taotao.cloud.ws.controller;

import cn.hutool.core.util.RandomUtil;
import com.taotao.cloud.ws.model.MessageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dengtao
 * @date 2020/12/18 下午3:20
 * @since v1.0
 */
@Controller
public class WebSocketController {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	// @SendTo("/topic/greetings")
	@Scheduled(fixedRate = 60000)
	public void test() {
		// simpMessagingTemplate.convertAndSend("/topic/greetings", new MessageResult(":我是服务器的数据"));
		int i = RandomUtil.randomInt(50);
		simpMessagingTemplate.convertAndSend("/topic/greetings", "近 " + i + "个月观众活跃趋势");
	}

	@MessageMapping("/hello") //接收"/app/hello"路径发送来的消息
	public void greetingSM(@RequestBody MessageParam message) {
		System.out.println(message);
		simpMessagingTemplate.convertAndSend("/topic/greetings", message);
	}
}
