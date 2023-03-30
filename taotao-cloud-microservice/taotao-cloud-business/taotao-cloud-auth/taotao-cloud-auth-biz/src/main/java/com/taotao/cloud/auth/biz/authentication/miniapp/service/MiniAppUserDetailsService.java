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

package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import org.springframework.security.core.userdetails.UserDetails;

/** The interface Channel user details service. */
public interface MiniAppUserDetailsService {

    /**
     * 小程序在微信登录成功后发起后端登录用来注册的方法
     *
     * @param request the request
     * @return the user details
     */
    UserDetails register(MiniAppRequest request, String sessionKey);

    /**
     * openid登录
     *
     * <p>clientId和openId决定唯一性
     *
     * @param clientId the client id
     * @param openId the open id
     * @return the user details
     */
    UserDetails loadByOpenId(String clientId, String openId);
}
