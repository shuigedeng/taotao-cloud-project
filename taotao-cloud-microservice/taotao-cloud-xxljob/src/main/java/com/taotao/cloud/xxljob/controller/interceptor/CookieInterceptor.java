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

package com.taotao.cloud.xxljob.controller.interceptor;

import com.taotao.cloud.xxljob.core.util.FtlUtil;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * push cookies to model as cookieMap
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class CookieInterceptor implements AsyncHandlerInterceptor {

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView)
            throws Exception {

        // cookie
        if (modelAndView != null
                && request.getCookies() != null
                && request.getCookies().length > 0) {
            HashMap<String, Cookie> cookieMap = new HashMap<String, Cookie>();
            for (Cookie ck : request.getCookies()) {
                cookieMap.put(ck.getName(), ck);
            }
            modelAndView.addObject("cookieMap", cookieMap);
        }

        // static method
        if (modelAndView != null) {
            modelAndView.addObject(
                    "I18nUtil", FtlUtil.generateStaticModel(I18nUtil.class.getName()));
        }
    }
}
