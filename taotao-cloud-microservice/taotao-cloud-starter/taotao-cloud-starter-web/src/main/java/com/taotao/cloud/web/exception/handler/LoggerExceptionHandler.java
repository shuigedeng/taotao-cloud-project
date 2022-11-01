package com.taotao.cloud.web.exception.handler;


import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的异常日志处理类
 */
public class LoggerExceptionHandler implements ExceptionHandler {

	private RequestMappingHandlerMapping mapping;

	public LoggerExceptionHandler(RequestMappingHandlerMapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public void handle(NativeWebRequest req, Throwable throwable, String traceId) {
		printLog(req, throwable);
	}

	/**
	 * 打印日志
	 *
	 * @param req 请求对象
	 * @param e   异常信息
	 * @since 2021-09-02 21:27:34
	 */
	private void printLog(NativeWebRequest req, Throwable e) {
		try {
			//RequestMappingHandlerMapping mapping = ContextUtils.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
			HandlerExecutionChain chain = mapping.getHandler(
				(HttpServletRequest) req.getNativeRequest());
			Object handler = chain.getHandler();
			if (handler instanceof HandlerMethod handlerMethod) {
				MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
				Object bean = handlerMethod.getBean();
			}
		} catch (Exception ex) {
			LogUtils.error(e);
		}

		LogUtils.error(e);
		LogUtils.error("【全局异常拦截】{}: 请求路径: {}, 请求参数: {}, 异常信息 {} ", e,
			e.getClass().getName(), uri(req), query(req), e.getMessage());
	}
}
