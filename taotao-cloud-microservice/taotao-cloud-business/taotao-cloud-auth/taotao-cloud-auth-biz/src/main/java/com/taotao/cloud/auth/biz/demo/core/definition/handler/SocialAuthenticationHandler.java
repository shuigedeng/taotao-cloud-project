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

package com.taotao.cloud.auth.biz.demo.core.definition.handler;

import cn.herodotus.engine.assistant.core.domain.AccessPrincipal;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import org.springframework.security.core.AuthenticationException;

/**
 * Description: 社交登录处理器
 *
 * @author : gengwei.zheng
 * @date : 2021/4/4 17:34
 */
public interface SocialAuthenticationHandler {

    /**
     * 社交登录
     *
     * <p>1. 首先在第三方系统进行认证，或者手机号码、扫码认证。返回认证后的信息 2. 根据认证返回的信息，在系统中查询是否有对应的用户信息。 2.1.
     * 如果有对应的信息，根据需要更新社交用户的信息，然后返回系统用户信息，进行登录。 2.2. 如果没有对应信息，就先进行用户的注册，然后进行社交用户和系统用户的绑定。
     *
     * @param source 社交登录提供者分类
     * @param accessPrincipal 社交登录所需要的信息 {@link AccessPrincipal}
     * @return 系统用户
     * @throws AuthenticationException {@link AuthenticationException} 认证错误
     */
    HerodotusUser authentication(String source, AccessPrincipal accessPrincipal) throws AuthenticationException;
}
