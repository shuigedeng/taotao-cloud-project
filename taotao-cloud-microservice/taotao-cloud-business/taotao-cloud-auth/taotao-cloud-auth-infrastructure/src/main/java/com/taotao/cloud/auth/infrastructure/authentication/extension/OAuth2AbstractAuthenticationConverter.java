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

package com.taotao.cloud.auth.infrastructure.authentication.extension;

import com.taotao.cloud.auth.infrastructure.crypto.HttpCryptoProcessor;
import com.taotao.cloud.auth.infrastructure.utils.OAuth2EndpointUtils;
import com.taotao.boot.security.spring.constants.OAuth2ErrorKeys;
import com.taotao.boot.security.spring.exception.SessionInvalidException;
import com.taotao.boot.security.spring.utils.ListUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * <p>抽象的认证 Converter </p>
 */
public abstract class OAuth2AbstractAuthenticationConverter implements AuthenticationConverter {

    private final HttpCryptoProcessor httpCryptoProcessor;

    public OAuth2AbstractAuthenticationConverter(HttpCryptoProcessor httpCryptoProcessor) {
        this.httpCryptoProcessor = httpCryptoProcessor;
    }

    protected String[] decrypt(String sessionId, List<String> parameters) {
        if (StringUtils.isNotBlank(sessionId) && CollectionUtils.isNotEmpty(parameters)) {
            List<String> result =
                    parameters.stream().map(item -> decrypt(sessionId, item)).toList();
            return ListUtils.toStringArray(result);
        }

        return ListUtils.toStringArray(parameters);
    }

    protected String decrypt(String sessionId, String parameter) {
        if (StringUtils.isNotBlank(sessionId) && StringUtils.isNotBlank(parameter)) {
            try {
                return httpCryptoProcessor.decrypt(sessionId, parameter);
            } catch (SessionInvalidException e) {
                OAuth2EndpointUtils.throwError(
                        OAuth2ErrorKeys.SESSION_EXPIRED,
                        e.getMessage(),
                        OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
            }
        }
        return parameter;
    }
}
