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

package com.taotao.cloud.im.biz.platform.common.shiro;

import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.ResultCodeEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.web.domain.AjaxResult;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

/** auth2过滤器 */
public class ShiroTokenFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse servletResponse) {
        // 获取请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return null;
        }
        return token;
    }

    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return error(response, ResultCodeEnum.UNAUTHORIZED, null);
        }
        try {
            getSubject(request, response).login(token);
            return true;
        } catch (LoginException e) {
            return error(response, ResultCodeEnum.UNAUTHORIZED, e.getMessage());
        } catch (AuthenticationException e) {
            return error(response, ResultCodeEnum.UNAUTHORIZED, null);
        }
    }

    private boolean error(ServletResponse response, ResultCodeEnum resultCode, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.getWriter().print(JSONUtil.toJsonStr(AjaxResult.result(resultCode, msg)));
        return false;
    }

    /** 获取请求的token */
    private ShiroLoginToken getToken(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(HeadConstant.TOKEN_KEY);
        if (!StringUtils.isEmpty(token)) {
            return new ShiroLoginToken(token);
        }
        return null;
    }
}
