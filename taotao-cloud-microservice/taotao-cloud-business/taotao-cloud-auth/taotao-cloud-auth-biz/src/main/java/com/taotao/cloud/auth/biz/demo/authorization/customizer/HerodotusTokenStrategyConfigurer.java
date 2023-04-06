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
import cn.herodotus.engine.assistant.core.enums.Target;
import cn.herodotus.engine.oauth2.authorization.converter.HerodotusJwtAuthenticationConverter;
import cn.herodotus.engine.oauth2.authorization.introspector.HerodotusOpaqueTokenIntrospector;
import cn.herodotus.engine.oauth2.core.properties.SecurityProperties;
import cn.herodotus.engine.oauth2.core.response.HerodotusAccessDeniedHandler;
import cn.herodotus.engine.oauth2.core.response.HerodotusAuthenticationEntryPoint;
import cn.herodotus.engine.web.core.properties.EndpointProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * Description: Token 配置 通用代码
 *
 * @author : gengwei.zheng
 * @date : 2022/10/14 17:29
 */
public class HerodotusTokenStrategyConfigurer {

    private JwtDecoder jwtDecoder;
    private SecurityProperties securityProperties;
    private EndpointProperties endpointProperties;
    private OAuth2ResourceServerProperties resourceServerProperties;
    private OpaqueTokenIntrospector opaqueTokenIntrospector;

    public HerodotusTokenStrategyConfigurer(
            JwtDecoder jwtDecoder,
            SecurityProperties securityProperties,
            EndpointProperties endpointProperties,
            OAuth2ResourceServerProperties resourceServerProperties) {
        this.jwtDecoder = jwtDecoder;
        this.securityProperties = securityProperties;
        this.endpointProperties = endpointProperties;
        this.resourceServerProperties = resourceServerProperties;
        this.opaqueTokenIntrospector =
                new HerodotusOpaqueTokenIntrospector(this.endpointProperties, this.resourceServerProperties);
    }

    private boolean isRemoteValidate() {
        return this.securityProperties.getValidate() == Target.REMOTE;
    }

    public OAuth2ResourceServerConfigurer<HttpSecurity> from(OAuth2ResourceServerConfigurer<HttpSecurity> configurer) {
        if (isRemoteValidate()) {
            configurer
                    .opaqueToken(opaque -> opaque.introspector(opaqueTokenIntrospector))
                    .accessDeniedHandler(new HerodotusAccessDeniedHandler())
                    .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint());
        } else {
            configurer
                    .jwt(jwt -> jwt.decoder(this.jwtDecoder)
                            .jwtAuthenticationConverter(new HerodotusJwtAuthenticationConverter()))
                    .bearerTokenResolver(new DefaultBearerTokenResolver())
                    .accessDeniedHandler(new HerodotusAccessDeniedHandler())
                    .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint());
        }
        return configurer;
    }

    public BearerTokenResolver createBearerTokenResolver() {
        return new HerodotusBearerTokenResolver(this.jwtDecoder, this.opaqueTokenIntrospector, this.isRemoteValidate());
    }
}
