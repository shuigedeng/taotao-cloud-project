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

package com.taotao.cloud.auth.biz.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 处理认证失败的逻辑
 *
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // String message = exceptionMessage(authException);
        // request.setAttribute("exMsg", message);
        // this.write(request, response);
    }

    // @Override
    // protected Map<String, Object> body(HttpServletRequest request) {
    //    Map<String, Object> map = new LinkedHashMap<>(3);
    //    String exMsg = (String) request.getAttribute("exMsg");
    //    map.put("code", HttpStatus.UNAUTHORIZED.value());
    //    map.put("uri", request.getRequestURI());
    //    map.put("message", exMsg);
    //    return map;
    // }

}
