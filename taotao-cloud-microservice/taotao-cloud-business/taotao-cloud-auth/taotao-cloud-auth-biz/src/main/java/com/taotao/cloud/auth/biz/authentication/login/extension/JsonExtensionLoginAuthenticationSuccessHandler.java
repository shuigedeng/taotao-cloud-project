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

package com.taotao.cloud.auth.biz.authentication.login.extension;

import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGenerator;
import com.taotao.cloud.common.utils.log.LogUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

/**
 * LoginAuthenticationSuccessHandler
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-11-07 10:26
 */
public class JsonExtensionLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    //
    //	public LoginAuthenticationSuccessHandler(HttpSecurity httpSecurity) {
    //		this.tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
    //	}
    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    private JwtTokenGenerator jwtTokenGenerator;

    public JsonExtensionLoginAuthenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        //		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
        //			.principal(authentication)
        //			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
        //			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        //			.authorizationGrant(authentication);
        //
        //		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        //		OAuth2Token oAuth2Token = tokenGenerator.generate(tokenContext);
        //
        //		// OAuth2AccessTokenResponse oAuth2AccessTokenResponse = jwtTokenGenerator.tokenResponse(
        //		//	(SecurityUser) authentication.getPrincipal());

        // OAuth2AccessTokenResponse oAuth2AccessTokenResponse = jwtTokenGenerator.tokenResponse(
        //	(SecurityUser) authentication.getPrincipal());

        //		Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
        //		if (MapUtil.isNotEmpty(map)) {
        //			// 发送异步日志事件
        //			PigUser userInfo = (PigUser) map.get(SecurityConstants.DETAILS_USER);
        //			log.info("用户：{} 登录成功", userInfo.getName());
        //			// 避免 race condition
        //			SecurityContext context = SecurityContextHolder.createEmptyContext();
        //			context.setAuthentication(accessTokenAuthentication);
        //			SecurityContextHolder.setContext(context);
        //			SysLog logVo = SysLogUtils.getSysLog();
        //			logVo.setTitle("登录成功");
        //			String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
        //			if (StrUtil.isNotBlank(startTimeStr)) {
        //				Long startTime = Long.parseLong(startTimeStr);
        //				Long endTime = System.currentTimeMillis();
        //				logVo.setTime(endTime - startTime);
        //			}
        //
        //			logVo.setServiceId(accessTokenAuthentication.getRegisteredClient().getClientId());
        //			logVo.setCreateBy(userInfo.getName());
        //			logVo.setUpdateBy(userInfo.getName());
        //			SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        //		}

        LogUtils.error("用户认证成功", authentication);
        OAuth2AccessTokenResponse accessTokenResponse =
                jwtTokenGenerator.tokenResponse((UserDetails) authentication.getPrincipal());
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);

        //		ResponseUtils.success(response, accessTokenResponse);
    }

    private void sendAccessTokenResponse(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }
}
