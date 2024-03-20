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
