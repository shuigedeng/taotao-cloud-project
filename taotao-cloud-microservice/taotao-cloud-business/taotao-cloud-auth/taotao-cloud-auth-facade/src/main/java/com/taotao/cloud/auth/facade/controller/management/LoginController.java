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

package com.taotao.cloud.auth.facade.controller.management;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

/**
 * <p>Security 登录控制器 </p>
 *
 *
 * @since : 2022/3/21 19:52
 * @see org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
 * @see org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
 */
@Controller
public class LoginController {

    private static final String DEFAULT_LOGIN_PAGE_VIEW = "login";
    private static final String DEFAULT_ERROR_PAGE_VIEW = "error";

    private final OAuth2AuthenticationProperties authenticationProperties;

    @Autowired
    public LoginController(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(DEFAULT_LOGIN_PAGE_VIEW);

        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        String errorMessage = getErrorMessage(request);

        Map<String, String> hiddenInputs = hiddenInputs(request);

        // 登录可配置用户名参数
        modelAndView.addObject("vulgar_tycoon", getFormLogin().getUsernameParameter());
        // 登录可配置密码参数
        modelAndView.addObject("beast", getFormLogin().getPasswordParameter());
        modelAndView.addObject("anubis", getFormLogin().getRememberMeParameter());
        modelAndView.addObject("graphic", getFormLogin().getCaptchaParameter());
        modelAndView.addObject("hide_verification_code", getFormLogin().getCloseCaptcha());
        // Security 隐藏域
        // AES加密key
        modelAndView.addObject("soup_spoon", SymmetricUtils.getEncryptedSymmetricKey());
        // 验证码类别
        modelAndView.addObject("verification_category", getFormLogin().getCategory());
        modelAndView.addObject("hidden_inputs", hiddenInputs);
        modelAndView.addObject("login_error", loginError);
        modelAndView.addObject("logout_success", logoutSuccess);
        modelAndView.addObject(
                "message", StringUtils.isNotBlank(errorMessage) ? HtmlUtils.htmlEscape(errorMessage) : null);
        modelAndView.addObject("contentPath", request.getContextPath());
        modelAndView.addObject("sessionId", WebUtils.getSessionId(request, true));

        return modelAndView;
    }

    private OAuth2AuthenticationProperties.FormLogin getFormLogin() {
        return authenticationProperties.getFormLogin();
    }

    private boolean isErrorPage(HttpServletRequest request) {
        String failureUrl = DEFAULT_LOGIN_PAGE_VIEW + "?" + DEFAULT_ERROR_PAGE_VIEW;
        return matches(request, failureUrl);
    }

    private boolean isLogoutSuccess(HttpServletRequest request) {
        String logoutSuccessUrl = DEFAULT_LOGIN_PAGE_VIEW + "?logout";
        return matches(request, logoutSuccessUrl);
    }

    private Map<String, String> hiddenInputs(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return (token != null)
                ? Collections.singletonMap(token.getParameterName(), token.getToken())
                : Collections.emptyMap();
    }

    private String getErrorMessage(HttpServletRequest request) {
        HttpSession session = WebUtils.getSession(request);
        if (ObjectUtils.isNotEmpty(session)) {
            String message = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ObjectUtils.isNotEmpty(message)) {
                return message;
            }
        }

        return null;
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!HttpMethod.GET.name().equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');
        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }
        return uri.equals(request.getContextPath() + url);
    }
}
