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

package com.taotao.cloud.auth.biz.dante.authorization.customizer;

import com.taotao.cloud.auth.biz.dante.authorization.converter.HerodotusJwtAuthenticationConverter;
import com.taotao.cloud.auth.biz.dante.authorization.introspector.HerodotusOpaqueTokenIntrospector;
import com.taotao.cloud.auth.biz.dante.authorization.properties.OAuth2AuthorizationProperties;
import com.taotao.cloud.auth.biz.dante.core.response.HerodotusAccessDeniedHandler;
import com.taotao.cloud.auth.biz.dante.core.response.HerodotusAuthenticationEntryPoint;
import com.taotao.cloud.auth.biz.dante.uaa.other.Target;
import com.taotao.cloud.core.properties.EndpointProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * <p>Description: Token 配置 通用代码 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/10/14 17:29
 */
public class HerodotusTokenStrategyConfigurer {

    private final JwtDecoder jwtDecoder;
    private final OAuth2AuthorizationProperties authorizationProperties;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    public HerodotusTokenStrategyConfigurer(OAuth2AuthorizationProperties authorizationProperties, JwtDecoder jwtDecoder, EndpointProperties endpointProperties, OAuth2ResourceServerProperties resourceServerProperties) {
        this.jwtDecoder = jwtDecoder;
        this.authorizationProperties = authorizationProperties;
        this.opaqueTokenIntrospector = new HerodotusOpaqueTokenIntrospector(endpointProperties, resourceServerProperties);
    }

    private boolean isRemoteValidate() {
        return this.authorizationProperties.getValidate() == Target.REMOTE;
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