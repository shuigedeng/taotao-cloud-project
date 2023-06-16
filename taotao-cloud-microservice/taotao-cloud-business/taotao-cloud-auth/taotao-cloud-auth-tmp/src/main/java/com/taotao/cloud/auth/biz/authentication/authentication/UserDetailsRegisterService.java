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

package com.taotao.cloud.auth.biz.authentication.authentication;

//import me.zhyd.oauth.model.AuthUser;

import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import com.taotao.cloud.auth.biz.uaa.exception.RegisterUserFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 用户名密码注册、手机短信登录与 OAuth 登录的用户注册接口.<br><br>
 * 推荐通过继承来实现 此接口的功能
 */
public interface UserDetailsRegisterService {

	/**
	 * 手机短信登录用户注册接口
	 *
	 * @param mobile 手机号
	 * @return 注册后的 UserDetails 信息
	 * @throws RegisterUserFailureException 用户注册失败
	 */
	default UserDetails registerUser(String mobile) throws RegisterUserFailureException {
		throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, null);
	}

	/**
	 * 用户名密码注册
	 *
	 * @param request request
	 * @return 注册后的 UserDetails 信息
	 * @throws RegisterUserFailureException 用户注册失败
	 */
	default UserDetails registerUser(ServletWebRequest request) throws RegisterUserFailureException {
		throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, null);
	}

	/**
	 * 第三方第一次登录成功后注册接口, <br>
	 * 默认方法直接抛出 {@link RegisterUserFailureException}.<br>
	 * 这里是为了兼容不需要第三方授权登录功能的应用, 特意设置为默认方法.
	 *
	 * @param authUser         {@link AuthUser}
	 * @param username         username(即本地系统的 userId), 通常情况下为 {@link AuthUser#getUsername()} 或
	 *                         {@link AuthUser#getUsername()} + "_" + {@link AuthUser#getSource()}
	 * @param defaultAuthority 第三方授权登录成功后的默认权限, 多个权限用逗号分开
	 * @return 注册后的 UserDetails 信息
	 * @throws RegisterUserFailureException 用户注册失败
	 */
//	@SuppressWarnings("NullableProblems")
//	default UserDetails registerUser(@NonNull AuthUser authUser, @NonNull String username, @NonNull String defaultAuthority) throws RegisterUserFailureException {
//		return this.registerUser(authUser, username, defaultAuthority, null);
//	}

	/**
	 * 第三方第一次登录成功后注册接口, 增加 OAuth2 过程中的 state 解密后的字符串信息 decoderState, 以便用户在 OAuth2 流程中添加自定义的信息,
	 * 与 {@link Auth2StateCoder} 配合使用.<br>
	 * 默认方法直接抛出 {@link RegisterUserFailureException}.<br>
	 * 这里是为了兼容不需要第三方授权登录功能的应用, 特意设置为默认方法. <br>
	 * <p>
	 * {@code https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7}
	 *
	 * @param authUser         {@link AuthUser}
	 * @param username         username(即本地系统的 userId), 通常情况下为 {@link AuthUser#getUsername()} 或
	 *                         {@link AuthUser#getUsername()} + "_" + {@link AuthUser#getSource()}
	 * @param defaultAuthority 第三方授权登录成功后的默认权限, 多个权限用逗号分开
	 * @param decodeState      OAuth2 过程中的 state 解密后的字符串信息
	 * @return 注册后的 UserDetails 信息
	 * @throws RegisterUserFailureException 用户注册失败
	 */
//	default UserDetails registerUser(@NonNull AuthUser authUser, @NonNull String username, @NonNull String defaultAuthority,
//									 @Nullable String decodeState) throws RegisterUserFailureException {
//		throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, null);
//	}

}
