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
package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * 一键登录服务
 */
public interface OneClickLoginService {

    /**
     * 根据 accessToken 从服务商获取用户手机号
     * @param accessToken   前端通过 sdk 获取的服务商的 access token
     * @param otherParamMap 其他请求参数 map(包括请求头参数), map(paramName, paramValue)
     * @return 手机号
     */
    @NonNull
    String callback(@NonNull String accessToken, @Nullable Map<String, String> otherParamMap);

    /**
     * 一键登录成功后, 针对 otherParamMap 的处理.
     * @param userDetails   登录成功后的 user details
     * @param otherParamMap 一键登录时的其他请求参数.
     */
    void otherParamsHandler(@NonNull UserDetails userDetails, @Nullable Map<String, String> otherParamMap);

}
