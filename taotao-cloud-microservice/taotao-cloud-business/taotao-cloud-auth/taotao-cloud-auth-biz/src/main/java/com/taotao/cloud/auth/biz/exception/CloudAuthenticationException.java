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

package com.taotao.cloud.auth.biz.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * AuthenticationException
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/22 15:36
 */
public class CloudAuthenticationException extends OAuth2AuthenticationException {

    public CloudAuthenticationException(String description) {
        super(description);
    }

    public CloudAuthenticationException(OAuth2Error error) {
        super(error);
    }

    public CloudAuthenticationException(OAuth2Error error, Throwable cause) {
        super(error, cause);
    }

    public CloudAuthenticationException(OAuth2Error error, String message) {
        super(error, message);
    }

    public CloudAuthenticationException(OAuth2Error error, String message, Throwable cause) {
        super(error, message, cause);
    }

    public static CloudAuthenticationException throwError(String description) {
        OAuth2Error error = new OAuth2Error("500", description, "");
        return new CloudAuthenticationException(error);
    }
}
