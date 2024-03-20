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

package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service;

import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

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
