package com.taotao.cloud.auth.biz.authentication.oauth2;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;

/**
 * 兼容微信token接口返回转换的工具，扩展了{@link DefaultMapOAuth2AccessTokenResponseConverter}
 *
 */
public class DelegateMapOAuth2AccessTokenResponseConverter implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

    private final Converter<Map<String, Object>, OAuth2AccessTokenResponse> delegate = new DefaultMapOAuth2AccessTokenResponseConverter();

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> tokenResponseParameters) {
        // 避免 token_type 空校验异常
        tokenResponseParameters.put(OAuth2ParameterNames.TOKEN_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());

        return this.delegate.convert(tokenResponseParameters);
    }

}
