/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.server.controller;

import cn.herodotus.engine.oauth2.authentication.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.core.utils.SymmetricUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import java.util.Collections;
import java.util.Map;

/**
 * <p>Description: Security 登录控制器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/21 19:52
 * @see org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
 * @see org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
 */
@Controller
public class LoginController {

    private static final String DEFAULT_LOGIN_PAGE_VIEW = "login";
    private static final String DEFAULT_ERROR_PAGE_VIEW = "error";

    private final OAuth2UiProperties uiProperties;

    @Autowired
    public LoginController(OAuth2UiProperties uiProperties) {
        this.uiProperties = uiProperties;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(DEFAULT_LOGIN_PAGE_VIEW);

        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        String errorMessage = getErrorMessage(request);

        Map<String, String> hiddenInputs = hiddenInputs(request);

        // 登录可配置用户名参数
        modelAndView.addObject("vulgar_tycoon", uiProperties.getUsernameParameter());
        // 登录可配置密码参数
        modelAndView.addObject("beast", uiProperties.getPasswordParameter());
        modelAndView.addObject("anubis", uiProperties.getRememberMeParameter());
        modelAndView.addObject("graphic", uiProperties.getCaptchaParameter());
        modelAndView.addObject("hide_verification_code", uiProperties.getCloseCaptcha());
        // Security 隐藏域
        // AES加密key
        modelAndView.addObject("soup_spoon", SymmetricUtils.getEncryptedSymmetricKey());
        // 验证码类别
        modelAndView.addObject("verification_category", uiProperties.getCategory());
        modelAndView.addObject("hidden_inputs", hiddenInputs);
        modelAndView.addObject("login_error", loginError);
        modelAndView.addObject("logout_success", logoutSuccess);
        modelAndView.addObject("message", StringUtils.isNotBlank(errorMessage) ? HtmlUtils.htmlEscape(errorMessage) : null);
        modelAndView.addObject("contentPath", request.getContextPath());

        String sessionId;
        HttpSession httpSession = request.getSession(false);
        if (ObjectUtils.isNotEmpty(httpSession)) {
            sessionId = httpSession.getId();
        } else {
            sessionId = request.getSession().getId();
        }
        modelAndView.addObject("sessionId", sessionId);

        return modelAndView;
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
        return (token != null) ? Collections.singletonMap(token.getParameterName(), token.getToken())
                : Collections.emptyMap();
    }

    private String getErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
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
