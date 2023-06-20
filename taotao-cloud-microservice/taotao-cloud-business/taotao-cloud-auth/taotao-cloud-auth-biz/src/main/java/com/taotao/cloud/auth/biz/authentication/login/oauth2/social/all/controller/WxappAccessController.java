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

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.dto.WxappProfile;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.processor.AccessHandlerStrategyFactory;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import com.taotao.cloud.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 微信小程序平台认证 </p>
 *
 * 
 * @date : 2021/5/28 11:40
 */
@RestController
@Tag(name = "微信小程序平台认证接口")
@RequestMapping
public class WxappAccessController {

	@Autowired
	private AccessHandlerStrategyFactory accessHandlerStrategyFactory;

	@Operation(summary = "微信小程序登录", description = "利用wx.login获取code，进行小程序登录")
	@Parameters({
		@Parameter(name = "socialDetails", required = true, description = "社交登录自定义参数实体"),
	})
	@PostMapping("/open/identity/wxapp")
	public Result<WxMaJscode2SessionResult> login(@Validated @RequestBody WxappProfile wxappProfile) {
		AccessResponse response = accessHandlerStrategyFactory.preProcess(AccountType.WXAPP, wxappProfile.getCode(), wxappProfile.getAppId());
		if (ObjectUtils.isNotEmpty(response)) {
			//微信小程序登录成功
			return Result.success( response.getSession());
		} else {
			//微信小程序登录失败
//			return Result.fail(null);
			return null;
		}
	}
}
