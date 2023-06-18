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

import com.google.common.collect.ImmutableMap;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.event.AutomaticSignInEvent;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.processor.JustAuthProcessor;
import com.taotao.cloud.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.zhyd.oauth.model.AuthCallback;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 社交登录第三方系统返回的Redirect Url </p>
 *
 * 
 * @date : 2021/5/28 11:35
 */
@RestController
@Tag(name = "社交登录Redirect Url")
public class JustAuthAccessController {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private JustAuthProcessor justAuthProcessor;

	@Operation(summary = "社交登录redirect url地址", description = "社交登录标准模式的redirect url地址，获取第三方登录返回的code")
	@Parameters({
		@Parameter(name = "source", required = true, description = "社交登录的类型，具体指定是哪一个第三方系统", in = ParameterIn.PATH),
	})
	@RequestMapping("/open/identity/social/{source}")
	public void callback(@PathVariable("source") String source, AuthCallback callback) {
		if (StringUtils.isNotBlank(source) && BeanUtil.isNotEmpty(callback)) {
			Map<String, Object> params = ImmutableMap.of("source", source, "callback", callback);
			applicationContext.publishEvent(new AutomaticSignInEvent(params));
		}
	}

	@Operation(summary = "获取社交登录列表", description = "根据后台已配置社交登录信息，返回可用的社交登录控制列表")
	@GetMapping("/open/identity/sources")
	public Result<Map<String, String>> list() {
		Map<String, String> list = justAuthProcessor.getAuthorizeUrls();
		if (MapUtils.isNotEmpty(list)) {
			return Result.success(list);
		} else {
			return Result.success(new HashMap<>());
		}
	}
}
