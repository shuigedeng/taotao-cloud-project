/*
 * Copyright (c) 2020 the original author or authors.
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

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.dubbo.IDubboDictService;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/test")
public class MessagesController {

	@Autowired
	private IFeignDictService feignDictService;

	@DubboReference(check = false)
	private IDubboDictService dubboDictService;

	@Operation(summary = "测试消息", description = "测试消息")
	@GetMapping("/messages")
	public String[] getMessages() {
		LogUtils.info("slfdlaskdf;lasjdf;lj");
		//DubboDictRes dubboDictRes = dubboDictService.findByCode(1);

		try {
			FeignDictResponse feignDictResponse = feignDictService.findByCode("sd");
		} catch (Exception e) {
			LogUtils.error(e);
			throw new RuntimeException(e);
		}


		return new String[]{"Message 1", "Message 2", "Message 3"};
	}
}
