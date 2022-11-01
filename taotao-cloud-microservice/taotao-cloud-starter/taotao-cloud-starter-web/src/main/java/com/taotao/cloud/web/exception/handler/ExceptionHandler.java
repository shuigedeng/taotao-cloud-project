package com.taotao.cloud.web.exception.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 异常日志处理类
 */
public interface ExceptionHandler {

	/**
	 * 在此处理错误信息 进行落库，入ES， 发送报警通知等信息
	 *
	 * @param req       异常
	 * @param throwable 异常
	 * @param traceId
	 */
	void handle(NativeWebRequest req, Throwable throwable, String traceId);

	/**
	 * 获取请求路径
	 *
	 * @param request 请求对象
	 * @return 请求路径
	 * @since 2021-09-02 21:27:08
	 */
	default String uri(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			return nativeRequest.getRequestURI();
		} else {
			return "--";
		}
	}

	/**
	 * 获取请求参数
	 *
	 * @param request 请求对象
	 * @return 请求参数
	 * @since 2021-09-02 21:27:14
	 */
	default String query(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			String queryString = nativeRequest.getQueryString();
			if (StrUtil.isNotBlank(queryString)) {
				return queryString;
			}
		}
		return "--";
	}
}
