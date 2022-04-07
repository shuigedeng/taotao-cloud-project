/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.auth.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessagesController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 15:56:32
 */
@Validated
@Tag(name = "测试API", description = "测试API")
@RestController
@RequestMapping("/auth")
public class MessagesController {

	@Operation(summary = "测试消息", description = "测试消息", method = CommonConstant.GET)
	@RequestLogger("获取验证码")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@GetMapping("/messages")
	public String[] getMessages() {
		return new String[]{"Message 1", "Message 2", "Message 3"};
	}
}
