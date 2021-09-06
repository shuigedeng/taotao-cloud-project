package com.taotao.cloud.prometheus.web;

import com.taotao.cloud.prometheus.annotation.ExceptionListener;
import com.taotao.cloud.prometheus.handler.ExceptionHandler;
import com.taotao.cloud.prometheus.properties.ExceptionNoticeProperties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class ExceptionNoticeHandlerResolver implements HandlerExceptionResolver {

	private final ExceptionHandler exceptionHandler;

	private final ExceptionNoticeProperties exceptionNoticeProperties;

	private final CurrentRequetBodyResolver currentRequetBodyResolver;

	private final CurrentRequestHeaderResolver currentRequestHeaderResolver;

	public ExceptionNoticeHandlerResolver(ExceptionHandler exceptionHandler,
		CurrentRequetBodyResolver currentRequetBodyResolver,
		CurrentRequestHeaderResolver currentRequestHeaderResolver,
		ExceptionNoticeProperties exceptionNoticeProperties) {
		this.exceptionHandler = exceptionHandler;
		this.currentRequestHeaderResolver = currentRequestHeaderResolver;
		this.currentRequetBodyResolver = currentRequetBodyResolver;
		this.exceptionNoticeProperties = exceptionNoticeProperties;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
		Object handler,
		Exception ex) {
		RuntimeException e = null;
		if (ex instanceof RuntimeException) {
			e = (RuntimeException) ex;
		}
		HandlerMethod handlerMethod = null;
		if (handler instanceof HandlerMethod) {
			handlerMethod = (HandlerMethod) handler;
		}
		ExceptionListener listener = getListener(handlerMethod);
		if (listener != null && e != null && handler != null) {
			exceptionHandler.createHttpNotice(e, request.getRequestURI(), getParames(request),
				getRequestBody(),
				getHeader(request));
		}
		return null;
	}

	private Map<String, String> getParames(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		request.getParameterMap()
			.forEach((x, y) -> map.put(x, String.join(" , ", Arrays.asList(y))));
		return map;
	}

	private String getRequestBody() {
		return currentRequetBodyResolver.getRequestBody();
	}

	private Map<String, String> getHeader(HttpServletRequest request) {
		return currentRequestHeaderResolver.headers(request,
			exceptionNoticeProperties.getIncludeHeaderName());
	}

	private ExceptionListener getListener(HandlerMethod handlerMethod) {
		ExceptionListener listener = handlerMethod.getMethodAnnotation(ExceptionListener.class);
		if (listener == null) {
			listener = handlerMethod.getBeanType().getAnnotation(ExceptionListener.class);
		}
		return listener;
	}

}
