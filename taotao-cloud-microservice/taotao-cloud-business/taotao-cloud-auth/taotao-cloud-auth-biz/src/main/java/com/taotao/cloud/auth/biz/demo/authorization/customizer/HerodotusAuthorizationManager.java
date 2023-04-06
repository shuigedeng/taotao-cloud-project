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

package com.taotao.cloud.auth.biz.demo.authorization.customizer;

import cn.herodotus.engine.oauth2.authorization.definition.HerodotusConfigAttribute;
import java.util.function.Supplier;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * Description: Spring Security 6 授权管理器
 *
 * <p>Spring Security 6 授权管理 1. 由原来的 AccessDecisionManager 和 AccessDecisionVoter，变更为使用 {@link
 * AuthorizationManager} 2. 原来的 SecurityMetadataSource 已经不再使用。其实想要自己扩展，基本逻辑还是一致。只不过给使用者更大的扩展度和灵活度。
 * 3. 原来的 <code>FilterSecurityInterceptor</code>，已经不再使用。改为使用 {@link
 * org.springframework.security.web.access.intercept.AuthorizationFilter}
 *
 * @author : gengwei.zheng
 * @date : 2022/11/8 14:57
 */
public class HerodotusAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final Logger log = LoggerFactory.getLogger(HerodotusAuthorizationManager.class);

    private final HerodotusSecurityMetadataSource herodotusSecurityMetadataSource;

    public HerodotusAuthorizationManager(HerodotusSecurityMetadataSource herodotusSecurityMetadataSource) {
        this.herodotusSecurityMetadataSource = herodotusSecurityMetadataSource;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        HerodotusConfigAttribute configAttribute = herodotusSecurityMetadataSource.getAttributes(object);
        if (ObjectUtils.isEmpty(configAttribute)) {
            return new AuthorizationDecision(true);
        }

        WebExpressionAuthorizationManager webExpressionAuthorizationManager =
                new WebExpressionAuthorizationManager(configAttribute.getAttribute());
        AuthorizationDecision decision = webExpressionAuthorizationManager.check(authentication, object);
        log.debug(
                "[Herodotus] |- Authorization decision for request [{}] is! [{}]",
                object.getRequest().getRequestURI(),
                decision);
        return decision;
    }
}
