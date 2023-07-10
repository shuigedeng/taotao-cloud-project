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

package com.taotao.cloud.auth.biz.authentication.login.extension;

import com.taotao.cloud.auth.biz.exception.IllegalParameterExtensionLoginException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * json处理程序扩展登录身份验证失败
 *
 * @author shuigedeng
 * @version 2023.07
 * @see AuthenticationFailureHandler
 * @since 2023-07-10 17:42:49
 */
public class JsonExtensionLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
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
        //	}

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

        String exceptionMsg = "用户认证失败";
        if (exception instanceof IllegalParameterExtensionLoginException illegalParameterExtensionLoginException) {
            LogUtils.error("用户参数校验异常", exception);
            exceptionMsg = illegalParameterExtensionLoginException.getMessage();
        }
        if (exception instanceof AuthenticationServiceException authenticationServiceException) {
            LogUtils.error("请求方式错误异常", exception);
            exceptionMsg = authenticationServiceException.getMessage();
        }
        if (exception instanceof UsernameNotFoundException usernameNotFoundException) {
            LogUtils.error("用户未找到", exception);
            exceptionMsg = usernameNotFoundException.getMessage();
        }
        if (exception instanceof BadCredentialsException badCredentialsException) {
            LogUtils.error("用户未找到", exception);
            exceptionMsg = badCredentialsException.getMessage();
        }
        if (exception instanceof InternalAuthenticationServiceException internalAuthenticationServiceException) {
            LogUtils.error("认证服务内部错误", exception);
            exceptionMsg = internalAuthenticationServiceException.getMessage();
        }

        if (exception instanceof LockedException lockedException) {
            LogUtils.error("用户已被锁定", exception);
            exceptionMsg = lockedException.getMessage();
        }
        if (exception instanceof DisabledException disabledException) {
            LogUtils.error("用户未启用", exception);
            exceptionMsg = disabledException.getMessage();
        }
        if (exception instanceof AccountExpiredException accountExpiredException) {
            LogUtils.error("用户账号已过期", exception);
            exceptionMsg = accountExpiredException.getMessage();
        }
        if (exception instanceof CredentialsExpiredException credentialsExpiredException) {
            LogUtils.error("用户账号已过期", exception);
            exceptionMsg = credentialsExpiredException.getMessage();
        }

        ResponseUtils.fail(response, exceptionMsg);
    }
}
