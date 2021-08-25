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
package com.taotao.cloud.security.security;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 用户未认证 接口需要特定的权限，但是当前用户是匿名用户或者是记住我的用户
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 09:55
 */
public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		LogUtil.error("用户未认证", authException);
		ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
	}
}
