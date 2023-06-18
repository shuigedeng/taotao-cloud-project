/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.controller;

import com.alibaba.nacos.api.model.v2.Result;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.processor.AccessHandlerStrategyFactory;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 手机验证码登录 </p>
 *
 * 
 * @date : 2021/5/28 11:39
 */
@RestController
@Tag(name = "手机验证码登录接口")
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
