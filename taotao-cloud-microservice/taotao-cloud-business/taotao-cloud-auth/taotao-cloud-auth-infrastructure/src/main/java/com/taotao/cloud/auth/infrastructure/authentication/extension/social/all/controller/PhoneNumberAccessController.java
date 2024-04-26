/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>手机验证码登录 </p>
 *
 *
 * @since : 2021/5/28 11:39
 */
@RestController
@Tag(name = "手机验证码登录接口")
@RequestMapping
public class PhoneNumberAccessController {

    //	@Autowired
    //	private AccessHandlerStrategyFactory accessHandlerStrategyFactory;
    //
    //	@Operation(summary = "手机验证码发送地址", description = "接收手机号码，发送验证码，并缓存至Redis")
    //	@Parameters({
    //		@Parameter(name = "mobile", required = true, description = "手机号码"),
    //	})
    //	@PostMapping("/open/identity/verification-code")
    //	public Result<String> sendCode(@RequestParam("mobile") String mobile) {
    //		AccessResponse response = accessHandlerStrategyFactory.preProcess(AccountType.SMS, mobile);
    //		if (ObjectUtils.isNotEmpty(response)) {
    //			if (response.getSuccess()) {
    //				return Result.success("短信发送成功！");
    //			} else {
    //				return Result.failure("短信发送失败！");
    //			}
    //		}
    //		return Result.failure("手机号码接收失败！");
    //	}
}
