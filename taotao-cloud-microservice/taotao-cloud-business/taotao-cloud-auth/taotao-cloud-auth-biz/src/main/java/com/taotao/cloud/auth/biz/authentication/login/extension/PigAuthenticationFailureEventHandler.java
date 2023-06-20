/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.biz.authentication.login.extension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @date 2022-06-02
 */
@Slf4j
public class PigAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

	private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 * request.
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) {
//		String username = request.getParameter(OAuth2ParameterNames.USERNAME);
//
//		log.info("用户：{} 登录失败，异常：{}", username, exception.getLocalizedMessage());
//		SysLog logVo = SysLogUtils.getSysLog();
//		logVo.setTitle("登录失败");
//		logVo.setType(LogTypeEnum.ERROR.getType());
//		logVo.setException(exception.getLocalizedMessage());
//		// 发送异步日志事件
//		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
//		if (StrUtil.isNotBlank(startTimeStr)) {
//			Long startTime = Long.parseLong(startTimeStr);
//			Long endTime = System.currentTimeMillis();
//			logVo.setTime(endTime - startTime);
//		}
//
//		logVo.setServiceId(WebUtils.getClientId());
//		logVo.setCreateBy(username);
//		logVo.setUpdateBy(username);
//		SpringContextHolder.publishEvent(new SysLogEvent(logVo));
//		// 写出错误信息
//		sendErrorResponse(request, response, exception);
	}

//	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException exception) throws IOException {
//		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
//		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
//		String errorMessage;
//
//		if (exception instanceof OAuth2AuthenticationException) {
//			OAuth2AuthenticationException authorizationException = (OAuth2AuthenticationException) exception;
//			errorMessage = StrUtil.isBlank(authorizationException.getError().getDescription())
//					? authorizationException.getError().getErrorCode()
//					: authorizationException.getError().getDescription();
//		}
//		else {
//			errorMessage = exception.getLocalizedMessage();
//		}
//
//		// 手机号登录
//		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//		if (SecurityConstants.APP.equals(grantType)) {
//			errorMessage = MsgUtils.getSecurityMessage("AbstractUserDetailsAuthenticationProvider.smsBadCredentials");
//		}
//
//		this.errorHttpResponseConverter.write(R.failed(errorMessage), MediaType.APPLICATION_JSON, httpResponse);
//	}

}
