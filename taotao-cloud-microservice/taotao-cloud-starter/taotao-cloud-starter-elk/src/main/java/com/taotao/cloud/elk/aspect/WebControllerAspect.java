/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.elk.aspect;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.SecurityConstant;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.elk.properties.ElkHealthLogStatisticProperties;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 切面获取入参和出参
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/15 11:28
 */
@Aspect
public class WebControllerAspect {

	private static final String[] TOKEN_KEYS = {SecurityConstant.BASE_AUTHORIZED,
		SecurityConstant.AUTHORIZED};

	@Pointcut("@within(org.springframework.stereotype.Controller) " +
		"|| @within(org.springframework.web.bind.annotation.RestController)")
	public void pointcut() {

	}

	@Autowired
	private ElkHealthLogStatisticProperties logStatisticProperties;

	@Around("pointcut()")
	public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
		Exception exception = null;
		Object result = null;
		long timeSpan = 0;

		HttpServletRequest request = RequestContextHolder.getRequestAttributes() == null ? null
			: ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		try {
			long start = System.currentTimeMillis();
			result = joinPoint.proceed();
			timeSpan = System.currentTimeMillis() - start;
		} catch (Exception e) {
			exception = e;
			throw e;
		} finally {
			if (logStatisticProperties.getEnabled()) {
				if (request != null) {
					String uri = request.getRequestURI().replace(request.getContextPath(), "");
					String inPutParam = preHandle(joinPoint, request);
					String outPutParam = postHandle(result);
					String ip = getRemoteHost(request);
					LogUtil.info("【远程ip】{},【url】{},【输入】{},【输出】{},【异常】{},【耗时】{}ms",
						ip,
						uri,
						inPutParam,
						outPutParam,
						exception == null ? "无" : StrUtil.nullToEmpty(exception.getMessage()),
						timeSpan);
				}
			}
		}

		return result;
	}

	private String preHandle(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		Annotation[] annotations = targetMethod.getAnnotations();
		StringBuilder sb = new StringBuilder();

		for (String tokenKey : TOKEN_KEYS) {
			String token = request.getHeader(tokenKey);
			if (StringUtil.isNotBlank(token)) {
				sb.append("token:").append(token).append(",");
				break;
			}
		}

		for (Annotation annotation : annotations) {
			if (!annotation.annotationType().toString()
				.contains("org.springframework.web.bind.annotation")) {
				continue;
			}
			sb.append(JsonUtil.toJSONString(request.getParameterMap()));
		}
		return sb.toString();
	}

	private String postHandle(Object retVal) {
		if (null == retVal) {
			return "";
		}
		return JsonUtil.toJSONString(retVal);
	}

	private String getRemoteHost(HttpServletRequest request) {
		String unknown = "unknown";
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

}
