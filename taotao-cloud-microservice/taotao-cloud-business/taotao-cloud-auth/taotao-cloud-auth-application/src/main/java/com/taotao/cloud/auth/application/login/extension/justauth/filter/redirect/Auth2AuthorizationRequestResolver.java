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

package com.taotao.cloud.auth.application.login.extension.justauth.filter.redirect;

import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Implementations of this interface are capable of resolving an
 * {@link OAuth2AuthorizationRequest} from the provided {@code HttpServletRequest}. Used
 * by the {@link Auth2DefaultRequestRedirectFilter} for resolving Authorization
 * Requests.
 *
 * @author Joe Grandja
 * @author Rob Winch
 * @author YongWu zheng
 * @see OAuth2AuthorizationRequest
 * @see Auth2DefaultRequestRedirectFilter
 * @since 5.1
 */
public interface Auth2AuthorizationRequestResolver {

    /**
     * Returns the {@link Auth2DefaultRequest} resolved from the provided
     * {@code HttpServletRequest} or {@code null} if not available.
     *
     * @param request the {@code HttpServletRequest}
     * @return the resolved {@link Auth2DefaultRequest} or {@code null} if not
     * available
     */
    Auth2DefaultRequest resolve(HttpServletRequest request);

    /**
     * Returns the {@link Auth2DefaultRequest} resolved from the provided
     * {@code HttpServletRequest} or {@code null} if not available.
     *
     * @param request              the {@code HttpServletRequest}
     * @param clientRegistrationId the clientRegistrationId to use
     * @return the resolved {@link Auth2DefaultRequest} or {@code null} if not
     * available
     */
    Auth2DefaultRequest resolve(HttpServletRequest request, String clientRegistrationId);
}
