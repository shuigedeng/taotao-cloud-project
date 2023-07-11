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

package com.taotao.cloud.auth.biz.authentication.token;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * jwt token generator
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:42
 */
public interface JwtTokenGenerator {

    /**
     * 令牌响应
     *
     * @param userDetails 用户详细信息
     * @return {@link OAuth2AccessTokenResponse }
     * @since 2023-07-10 17:25:43
     */
    OAuth2AccessTokenResponse tokenResponse(UserDetails userDetails);

    /**
     * 社交令牌响应
     *
     * @param oAuth2User o auth2用户
     * @return {@link OAuth2AccessTokenResponse }
     * @since 2023-07-10 17:25:43
     */
    OAuth2AccessTokenResponse socialTokenResponse(OAuth2User oAuth2User);
}
