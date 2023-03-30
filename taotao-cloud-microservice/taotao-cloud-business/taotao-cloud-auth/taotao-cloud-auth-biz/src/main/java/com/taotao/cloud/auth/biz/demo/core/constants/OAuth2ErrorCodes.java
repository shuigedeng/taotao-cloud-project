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

package com.taotao.cloud.auth.biz.demo.core.constants;

/**
 * Description: 扩展 OAuth2 错误代码
 *
 * @author : gengwei.zheng
 * @date : 2022/7/9 12:59
 */
public interface OAuth2ErrorCodes {

    public static final String INVALID_REQUEST = "invalid_request";
    public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
    public static final String ACCESS_DENIED = "access_denied";
    public static final String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
    public static final String INVALID_SCOPE = "invalid_scope";
    public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String SERVER_ERROR = "server_error";
    public static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";
    public static final String INVALID_CLIENT = "invalid_client";
    public static final String INVALID_GRANT = "invalid_grant";
    public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
    public static final String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";
    public static final String INVALID_REDIRECT_URI = "invalid_redirect_uri";

    String ACCOUNT_EXPIRED = "AccountExpiredException";
    String ACCOUNT_DISABLED = "DisabledException";
    String ACCOUNT_LOCKED = "LockedException";
    String ACCOUNT_ENDPOINT_LIMITED = "AccountEndpointLimitedException";
    String BAD_CREDENTIALS = "BadCredentialsException";
    String CREDENTIALS_EXPIRED = "CredentialsExpiredException";
    String USERNAME_NOT_FOUND = "UsernameNotFoundException";

    String SESSION_EXPIRED = "SessionExpiredException";
}
