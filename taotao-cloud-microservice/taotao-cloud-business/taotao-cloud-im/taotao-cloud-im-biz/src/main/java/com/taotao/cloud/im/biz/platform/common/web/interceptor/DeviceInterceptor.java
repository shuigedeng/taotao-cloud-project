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

package com.taotao.cloud.im.biz.platform.common.web.interceptor;

import com.platform.common.constant.HeadConstant;
import com.platform.common.web.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/** 设备拦截器 */
@Component
public class DeviceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String device = ServletUtil.getHeader(request, HeadConstant.DEVICE, CharsetUtil.UTF_8);
        if (StringUtils.isEmpty(device)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(AjaxResult.fail("请求不正确")));
            return false;
        }
        return true;
    }
}
