/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.core.constants;

/**
 * <p>Description: 扩展 OAuth2 错误代码 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/9 12:59
 */
public interface OAuth2ErrorKeys {

    String INVALID_REQUEST = "invalid_request";
    String UNAUTHORIZED_CLIENT = "unauthorized_client";
    String ACCESS_DENIED = "access_denied";
    String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
    String INVALID_SCOPE = "invalid_scope";
    String INSUFFICIENT_SCOPE = "insufficient_scope";
    String INVALID_TOKEN = "invalid_token";
    String SERVER_ERROR = "server_error";
    String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";
    String INVALID_CLIENT = "invalid_client";
    String INVALID_GRANT = "invalid_grant";
    String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
    String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";
    String INVALID_REDIRECT_URI = "invalid_redirect_uri";

    String ACCOUNT_EXPIRED = "AccountExpiredException";
    String ACCOUNT_DISABLED = "DisabledException";
    String ACCOUNT_LOCKED = "LockedException";
    String ACCOUNT_ENDPOINT_LIMITED = "AccountEndpointLimitedException";
    String BAD_CREDENTIALS = "BadCredentialsException";
    String CREDENTIALS_EXPIRED = "CredentialsExpiredException";
    String USERNAME_NOT_FOUND = "UsernameNotFoundException";

    String SESSION_EXPIRED = "SessionExpiredException";

}
