/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth;

import com.taotao.cloud.auth.biz.uaa.exception.RefreshTokenFailureException;
import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import com.xkcoding.http.exception.SimpleHttpException;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.AuthChecker;
import me.zhyd.oauth.utils.StringUtils;
import me.zhyd.oauth.utils.UuidUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link AuthDefaultRequest} 的适配器
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020.11.19 12:35
 */
@Slf4j
public class AuthDefaultRequestAdapter extends AuthDefaultRequest implements Auth2DefaultRequest {

	private final String providerId;

	private AuthDefaultRequest authDefaultRequest;

	/**
	 * 构造 {@link AuthDefaultRequest} 的适配器
	 *
	 * @param config         {@link AuthDefaultRequest} 的 {@link AuthConfig}
	 * @param source         {@link AuthDefaultRequest} 的 {@link AuthSource}
	 * @param authStateCache {@link AuthDefaultRequest} 的 {@link AuthStateCache}
	 */
	public AuthDefaultRequestAdapter(AuthConfig config, AuthSource source, AuthStateCache authStateCache) {
		super(config, source, authStateCache);
		String providerId = Auth2RequestHolder.getProviderId(source);
		if (org.springframework.util.StringUtils.hasText(providerId)) {
			this.providerId = providerId;
		} else {
			throw new RuntimeException("AuthSource 必须是 me.zhyd.oauth.config.AuthDefaultSource 或 top.dcenter.ums.security.core.oauth.justauth.source.AuthCustomizeSource 子类");
		}
	}

	public void setAuthDefaultRequest(AuthDefaultRequest authDefaultRequest) {
		this.authDefaultRequest = authDefaultRequest;
	}

	@Override
	public String getRealState(String state) {
		if (StringUtils.isEmpty(state)) {
			state = UuidUtils.getUUID();
		}

		// 缓存 state
		this.authStateCache.cache(state, state);
		return state;
	}

	/**
	 * 统一的登录入口。当通过{@link AuthDefaultRequest#authorize(String)}授权成功后，会跳转到调用方的相关回调方法中
	 * 方法的入参可以使用{@code AuthCallback}，{@code AuthCallback}类中封装好了OAuth2授权回调所需要的参数
	 *
	 * @param authCallback 用于接收回调参数的实体
	 * @return AuthResponse
	 * @see AuthDefaultRequest#login(AuthCallback)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public AuthResponse login(AuthCallback authCallback) {
		try {
			AuthChecker.checkCode(this.source, authCallback);
			if (!this.config.isIgnoreCheckState()) {
				AuthChecker.checkState(authCallback.getState(), this.source, this.authStateCache);
			}

			AuthToken authToken = this.getAccessToken(authCallback);
			AuthUser user = this.getUserInfo(authToken);
			return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(user).build();
		} catch (Exception e) {
			log.error("Failed to login with oauth authorization. error: " + e.getMessage(), e);
			return Auth2DefaultRequest.responseError(e);
		}
	}

	@Override
	public AuthTokenPo refreshToken(AuthTokenPo authToken) throws SimpleHttpException, AuthException, RefreshTokenFailureException {
		if (this.authDefaultRequest == null) {
			throw new RuntimeException("AuthDefaultRequest 不能为 null 值, 必须通过方法 setAuthDefaultRequest(AuthDefaultRequest) 设置");
		}
		//noinspection rawtypes
		AuthResponse authResponse = this.authDefaultRequest.refresh(authToken);
		return Auth2DefaultRequest.getAuthTokenPo(this.config.getHttpConfig().getTimeout(), authToken.getId(),
			authResponse);
	}

	@Override
	public AuthSource getAuthSource() {
		return this.source;
	}

	@Override
	public AuthStateCache getAuthStateCache() {
		return this.authStateCache;
	}

	/**
	 * 获取access token
	 *
	 * @param authCallback 授权成功后的回调参数
	 * @return token
	 * @see AuthDefaultRequest#authorize(String)
	 */
	@Override
	public AuthToken getAccessToken(AuthCallback authCallback) throws SimpleHttpException {
		try {

			Method method = getMethod("getAccessToken", AuthCallback.class);
			Object result = method.invoke(this.authDefaultRequest, authCallback);
			return (AuthToken) result;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			String errMsg = e.getMessage();
			if (e instanceof InvocationTargetException) {
				InvocationTargetException invocationTargetException = ((InvocationTargetException) e);
				errMsg = invocationTargetException.getTargetException().getMessage();
			}
			String msg = "从第三方获取 accessToken 时方法调用异常: " + errMsg;
			throw new SimpleHttpException(msg, e);
		}
	}

	/**
	 * 使用token换取用户信息
	 *
	 * @param authToken token信息
	 * @return 用户信息
	 * @see AuthDefaultRequest#getAccessToken(AuthCallback)
	 */
	@Override
	@Nullable
	public AuthUser getUserInfo(AuthToken authToken) throws SimpleHttpException {
		try {
			Method method = getMethod("getUserInfo", AuthToken.class);
			Object result = method.invoke(this.authDefaultRequest, authToken);
			return (AuthUser) result;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			String errMsg = e.getMessage();
			if (e instanceof InvocationTargetException) {
				InvocationTargetException invocationTargetException = ((InvocationTargetException) e);
				errMsg = invocationTargetException.getTargetException().getMessage();
			}
			String msg = "从第三方获取用户信息时方法调用异常: " + errMsg;
			throw new SimpleHttpException(msg, e);
		}
	}


	@Override
	public String getProviderId() {
		return this.providerId;
	}

	@Override
	public String authorize(String state) {
		if (this.authDefaultRequest == null) {
			throw new RuntimeException("AuthDefaultRequest 不能为 null 值, 必须通过方法 setAuthDefaultRequest(AuthDefaultRequest) 设置");
		}
		return this.authDefaultRequest.authorize(state);
	}

	private Method getMethod(@NonNull String methodName, @NonNull Class<?>... parameterTypes) throws NoSuchMethodException {
		final Method method = this.authDefaultRequest.getClass().getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method;
	}

}
