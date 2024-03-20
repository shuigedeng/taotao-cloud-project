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

package com.taotao.cloud.auth.application.login.extension.wechatminiapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.auth.application.login.extension.wechatminiapp.client.WechatLoginResponse;
import com.taotao.cloud.auth.application.login.extension.wechatminiapp.client.WechatMiniAppClient;
import com.taotao.cloud.auth.application.login.extension.wechatminiapp.service.WechatMiniAppClientService;
import com.taotao.cloud.auth.application.login.extension.wechatminiapp.service.WechatMiniAppSessionKeyCacheService;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 小程序预授权
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OncePerRequestFilter
 * @since 2023-07-13 12:05:00
 */
public class WechatMiniAppPreAuthenticationFilter extends OncePerRequestFilter {

    private static final String ENDPOINT = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String MINI_CLIENT_KEY = "clientId";
    private static final String JS_CODE_KEY = "jsCode";
    private final RequestMatcher requiresAuthenticationRequestMatcher =
            new AntPathRequestMatcher("/login/wechat/miniapp/preauth", "GET");
    private final ObjectMapper om = new ObjectMapper();
    private final WechatMiniAppClientService wechatMiniAppClientService;
    private final WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService;
    private final RestOperations restOperations;

    /**
     * Instantiates a new Mini app pre authentication filter.
     *
     * @param wechatMiniAppClientService the mini app client service
     * @param wechatMiniAppSessionKeyCacheService the mini app session key cache
     */
    public WechatMiniAppPreAuthenticationFilter(
            WechatMiniAppClientService wechatMiniAppClientService,
            WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService) {
        this.wechatMiniAppClientService = wechatMiniAppClientService;
        this.wechatMiniAppSessionKeyCacheService = wechatMiniAppSessionKeyCacheService;
        this.restOperations = new RestTemplate();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (response.isCommitted()) {
            return;
        }

        if (requiresAuthenticationRequestMatcher.matches(request)) {
            String clientId = request.getParameter(MINI_CLIENT_KEY);
            String jsCode = request.getParameter(JS_CODE_KEY);
            WechatMiniAppClient wechatMiniAppClient = wechatMiniAppClientService.get(clientId);
            WechatLoginResponse responseEntity = this.getResponse(wechatMiniAppClient, jsCode);

            String openId = responseEntity.getOpenid();
            String sessionKey = responseEntity.getSessionKey();
            wechatMiniAppSessionKeyCacheService.put(clientId + "::" + openId, sessionKey);
            responseEntity.setSessionKey(null);
            ResponseUtils.success(response, Result.success(responseEntity));
            return;
        }
        filterChain.doFilter(request, response);
    }

    // private static class PreAuthResponseWriter extends ResponseWriter {
    //
    // 	@Override
    // 	protected Map<String, Object> body(HttpServletRequest request) {
    // 		WechatLoginResponse miniAuth = (WechatLoginResponse) request.getAttribute(ATTRIBUTE_KEY);
    // 		Map<String, Object> map = new HashMap<>(3);
    // 		map.put("code", HttpStatus.OK.value());
    // 		map.put("data", miniAuth);
    // 		map.put("message", HttpStatus.OK.getReasonPhrase());
    // 		return map;
    // 	}
    // }

    /**
     * 请求微信服务器登录接口 code2session
     *
     * @param wechatMiniAppClient miniAppClient
     * @param jsCode jsCode
     * @return ObjectNode
     */
    private WechatLoginResponse getResponse(WechatMiniAppClient wechatMiniAppClient, String jsCode)
            throws JsonProcessingException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("appid", wechatMiniAppClient.getAppId());
        queryParams.add("secret", wechatMiniAppClient.getSecret());
        queryParams.add("js_code", jsCode);
        queryParams.add("grant_type", "authorization_code");

        URI uri = UriComponentsBuilder.fromHttpUrl(ENDPOINT)
                .queryParams(queryParams)
                .build()
                .toUri();
        String response = restOperations.getForObject(uri, String.class);

        if (Objects.isNull(response)) {
            throw new BadCredentialsException("miniapp response is null");
        }
        return om.readValue(response, WechatLoginResponse.class);
    }
}
