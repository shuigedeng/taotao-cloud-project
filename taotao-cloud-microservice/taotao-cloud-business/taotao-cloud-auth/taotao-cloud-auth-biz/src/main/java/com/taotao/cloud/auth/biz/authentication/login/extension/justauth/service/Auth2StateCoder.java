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
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.service;

import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.lang.NonNull;

/**
 * 对 OAuth2 login 流程中的 state 进行自定义编解码. 可以传递必要的信息, 如: 第三方登录成功的跳转地址等<br>
 * 注意此接口的两个方法必须同时实现对应的编解码逻辑, 实现此接口后注入 IOC 容器即可, 如有前端向后端获取 authorizeUrl 时向后端传递额外参数
 * 且用作注册时的信息, 需配合 {@link UmsUserDetailsService#registerUser(AuthUser, String, String, String)} 方法实现.<br>
 * {@code https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7}
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/26 10:06
 */
public interface Auth2StateCoder {

	/**
	 * 对 state 进行编码, 通过 {@link HttpServletRequest}, 方便对 OAuth2 login 过程中, 前端向后端获取 authorizeUrl 时
	 * 向后端传递额外的参数, 从而编码进 state 中, 在 OAuth2 login 流程中传递参数.<br>
	 * 注意: 对 state 进行对称编码时注意增加混淆逻辑, 以免信息泄露.
	 *
	 * @param state   state 字符串, 如为未实现 {@link Auth2DefaultRequest#generateState()}, 默认传入的是 UUID
	 * @param request {@link HttpServletRequest}, 方便对 OAuth2 login 过程中, 前端向后端获取 authorizeUrl 时向后端传递额外参数,
	 *                从而编码进 state 中, 在 OAuth2 login 流程中传递参数.
	 * @return 返回编码后的 state
	 */
	String encode(@NonNull String state, @NonNull HttpServletRequest request);

	/**
	 * 对 encoderState 进行解码
	 *
	 * @param encoderState 编码后的 state
	 * @return 返回解码后的 state
	 */
	String decode(@NonNull String encoderState);
}
