/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.interceptor;

import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器： 将请求头数据，封装到BaseContextHandler(ThreadLocal)
 * <p>
 * 该拦截器要优先于系统中其他的业务拦截器
 * <p>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:01:25
 */
public class HeaderThreadLocalInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		//if (!(handler instanceof HandlerMethod)) {
		//	return false;
		//}

        //if (!ContextUtil.getBoot()) {
        //    ContextUtil.setUserId(getHeader(request, ContextConstants.JWT_KEY_USER_ID));
        //    ContextUtil.setAccount(getHeader(request, ContextConstants.JWT_KEY_ACCOUNT));
        //    ContextUtil.setName(getHeader(request, ContextConstants.JWT_KEY_NAME));
        //    ContextUtil.setTenant(getHeader(request, ContextConstants.JWT_KEY_TENANT));
		//
        //    String traceId = request.getHeader(ContextConstants.TRACE_ID_HEADER);
        //    MDC.put(ContextConstants.LOG_TRACE_ID, StrUtil.isEmpty(traceId) ? StrUtil.EMPTY : traceId);
        //    MDC.put(ContextConstants.JWT_KEY_TENANT, getHeader(request, ContextConstants.JWT_KEY_TENANT));
        //    MDC.put(ContextConstants.JWT_KEY_USER_ID, getHeader(request, ContextConstants.JWT_KEY_USER_ID));
        //}
        //ContextUtil.setGrayVersion(getHeader(request, ContextConstants.GRAY_VERSION));

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
		Object handler, Exception ex) throws Exception {
//        ContextUtil.remove();
	}
}
