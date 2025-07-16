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

package com.taotao.cloud.auth.biz.authentication.device;

import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * 设备码模式token
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:24:22
 */
@Transient
public class DeviceClientAuthenticationToken extends OAuth2ClientAuthenticationToken {

    /**
     * 设备客户端身份验证令牌
     *
     * @param clientId                   客户端id
     * @param clientAuthenticationMethod 客户端身份验证方法
     * @param credentials                凭据
     * @param additionalParameters       附加参数
     * @return
     * @since 2023-07-10 17:24:24
     */
    public DeviceClientAuthenticationToken(
            String clientId,
            ClientAuthenticationMethod clientAuthenticationMethod,
            @Nullable Object credentials,
            @Nullable Map<String, Object> additionalParameters) {
        super(clientId, clientAuthenticationMethod, credentials, additionalParameters);
    }

    /**
     * 设备客户端身份验证令牌
     *
     * @param registeredClient           注册客户
     * @param clientAuthenticationMethod 客户端身份验证方法
     * @param credentials                凭据
     * @return
     * @since 2023-07-10 17:24:24
     */
    public DeviceClientAuthenticationToken(
            RegisteredClient registeredClient,
            ClientAuthenticationMethod clientAuthenticationMethod,
            @Nullable Object credentials) {
        super(registeredClient, clientAuthenticationMethod, credentials);
    }
}
