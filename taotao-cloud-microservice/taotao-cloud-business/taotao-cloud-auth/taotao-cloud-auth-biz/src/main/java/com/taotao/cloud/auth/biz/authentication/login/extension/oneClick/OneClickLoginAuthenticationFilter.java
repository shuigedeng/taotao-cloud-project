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

package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick;

import static java.util.Objects.nonNull;

import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickLoginService;
import com.taotao.cloud.auth.biz.exception.Auth2Exception;
import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import com.taotao.cloud.common.utils.context.ContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 一键登录过滤器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-16 13:51:44
 */
public class OneClickLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login/oneClick", "POST");

    private final String tokenParamName;
    /**
     * 其他请求参数名称列表(包括请求头名称)
     */
    private List<String> otherParamNames;

    private OneClickLoginService oneClickLoginService;
    private boolean postOnly = true;

    public OneClickLoginAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);

        this.oneClickLoginService = ContextUtils.getBean(OneClickLoginService.class, true);
        OneClickLoginProperties oneClickLoginProperties = ContextUtils.getBean(OneClickLoginProperties.class, true);

        this.tokenParamName = oneClickLoginProperties.getTokenParamName();
        this.otherParamNames = oneClickLoginProperties.getOtherParamNames();
    }

    public OneClickLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);

        this.oneClickLoginService = ContextUtils.getBean(OneClickLoginService.class, true);
        OneClickLoginProperties oneClickLoginProperties = ContextUtils.getBean(OneClickLoginProperties.class, true);

        this.tokenParamName = oneClickLoginProperties.getTokenParamName();
        this.otherParamNames = oneClickLoginProperties.getOtherParamNames();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String accessToken = obtainAccessToken(request);
        if (StrUtil.isEmpty(accessToken)) {
            throw new Auth2Exception(ErrorCodeEnum.ACCESS_TOKEN_NOT_EMPTY, this.tokenParamName);
        }

        accessToken = accessToken.trim();
        Map<String, String> otherParamMap = getOtherParamMap(this.otherParamNames, request);
        String mobile = this.oneClickLoginService.callback(accessToken, otherParamMap);

        OneClickLoginAuthenticationToken authRequest = new OneClickLoginAuthenticationToken(mobile, otherParamMap);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        // 一键登录: 用户已注册则登录, 未注册则自动注册用户且返回登录状态
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 从 request 中获取 otherParamNames 的 paramValue
     *
     * @param otherParamNames 参数名称列表
     * @param request         request
     * @return map(paramName, paramValue)
     */
    @Nullable
    protected Map<String, String> getOtherParamMap(
            @NonNull List<String> otherParamNames, @NonNull HttpServletRequest request) {
        if (otherParamNames.isEmpty()) {
            return null;
        }
        // map(paramName, paramValue)
        final Map<String, String> otherMap = new HashMap<>(otherParamNames.size());
        otherParamNames.forEach(name -> {
            try {
                String value = ServletRequestUtils.getStringParameter(request, name);
                otherMap.put(name, value);
            } catch (ServletRequestBindingException e) {
                String headerValue = request.getHeader(name);
                if (nonNull(headerValue)) {
                    otherMap.put(name, headerValue);
                }
            }
        });

        return otherMap;
    }

    /**
     * 获取 access token;
     *
     * @param request so that request attributes can be retrieved
     * @return access token
     */
    protected String obtainAccessToken(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request, null);
        return servletWebRequest.getParameter(tokenParamName);
    }

    /**
     * Provided so that subclasses may configure what is put into the auth
     * request's details property.
     *
     * @param request     that an auth request is being created for
     * @param authRequest the auth request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request, OneClickLoginAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an auth request is received which is not a POST request, an
     * exception will be raised immediately and auth will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * auth.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getTokenParamName() {
        return tokenParamName;
    }

    public List<String> getOtherParamNames() {
        return otherParamNames;
    }

    public void setOtherParamNames(List<String> otherParamNames) {
        this.otherParamNames = otherParamNames;
    }

    public OneClickLoginService getOneClickLoginService() {
        return oneClickLoginService;
    }

    public void setOneClickLoginService(OneClickLoginService oneClickLoginService) {
        this.oneClickLoginService = oneClickLoginService;
    }

    public boolean isPostOnly() {
        return postOnly;
    }
}
