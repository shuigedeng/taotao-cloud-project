package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.web.annotation.IgnoreResponseBodyAdvice;
import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一返回值 包装器
 *
 * @author zuihou
 * @date 2020/12/30 2:48 下午
 */
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"}, annotations = {
//	RestController.class, Controller.class})
@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"})
public class ResponseConfiguration implements ResponseBodyAdvice {

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		// 类上如果被 IgnoreResponseBodyAdvice 标识就不拦截
		if (methodParameter.getDeclaringClass()
			.isAnnotationPresent(IgnoreResponseBodyAdvice.class)) {
			return false;
		}

		// 方法上被标注也不拦截
		return !methodParameter.getMethod().isAnnotationPresent(IgnoreResponseBodyAdvice.class);
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
		Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		if (o == null) {
			return null;
		}
		if (o instanceof Result) {
			return o;
		}

		return Result.success(o);
	}
}
