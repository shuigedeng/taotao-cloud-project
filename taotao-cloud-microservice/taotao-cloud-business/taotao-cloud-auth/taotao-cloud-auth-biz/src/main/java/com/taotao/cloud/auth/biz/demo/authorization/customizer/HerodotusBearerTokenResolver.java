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

import cn.herodotus.engine.assistant.core.definition.BearerTokenResolver;
import cn.herodotus.engine.assistant.core.domain.PrincipalDetails;
import cn.herodotus.engine.oauth2.core.utils.PrincipalUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

public class HerodotusBearerTokenResolver implements BearerTokenResolver {

    private static final Logger log = LoggerFactory.getLogger(HerodotusBearerTokenResolver.class);

    private final JwtDecoder jwtDecoder;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;
    private final boolean isRemoteValidate;

    public HerodotusBearerTokenResolver(
            JwtDecoder jwtDecoder,
            OpaqueTokenIntrospector opaqueTokenIntrospector,
            boolean isRemoteValidate) {
        this.jwtDecoder = jwtDecoder;
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        this.isRemoteValidate = isRemoteValidate;
    }

    @Override
    public PrincipalDetails resolve(String token) {

        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token can not be null");
        }

        BearerTokenAuthenticationToken bearer = new BearerTokenAuthenticationToken(token);

        if (isRemoteValidate) {
            OAuth2AuthenticatedPrincipal principal = getOpaque(bearer);
            if (ObjectUtils.isNotEmpty(principal)) {
                PrincipalDetails details = PrincipalUtils.toPrincipalDetails(principal);
                log.debug("[Herodotus] |- Resolve OPAQUE token to principal details [{}]", details);
                return details;
            }
        } else {
            Jwt jwt = getJwt(bearer);
            if (ObjectUtils.isNotEmpty(jwt)) {
                PrincipalDetails details = PrincipalUtils.toPrincipalDetails(jwt);
                log.debug("[Herodotus] |- Resolve JWT token to principal details [{}]", details);
                return details;
            }
        }

        return null;
    }

    private Jwt getJwt(BearerTokenAuthenticationToken bearer) {
        try {
            return this.jwtDecoder.decode(bearer.getToken());
        } catch (BadJwtException failed) {
            log.warn("[Herodotus] |- Failed to decode since the JWT was invalid");
        } catch (JwtException failed) {
            log.warn("[Herodotus] |- Failed to decode JWT, catch exception", failed);
        }

        return null;
    }

    private OAuth2AuthenticatedPrincipal getOpaque(BearerTokenAuthenticationToken bearer) {
        try {
            return this.opaqueTokenIntrospector.introspect(bearer.getToken());
        } catch (BadOpaqueTokenException failed) {
            log.warn("Failed to introspect since the Opaque was invalid");
        } catch (OAuth2IntrospectionException failed) {
            log.warn("[Herodotus] |- Failed to introspect Opaque, catch exception", failed);
        }

        return null;
    }
}
