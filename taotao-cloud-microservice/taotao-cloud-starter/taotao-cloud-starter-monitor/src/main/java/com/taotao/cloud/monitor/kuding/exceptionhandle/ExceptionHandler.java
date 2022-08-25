package com.taotao.cloud.monitor.kuding.exceptionhandle;

import com.taotao.cloud.monitor.kuding.exceptionhandle.event.ExceptionNoticeEvent;
import com.taotao.cloud.monitor.kuding.pojos.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.pojos.HttpExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.PromethreusNoticeProperties;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ExceptionHandler {

	private final PromethreusNoticeProperties noticeProperties;

	private final ExceptionNoticeProperties exceptionNoticeProperties;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @param noticeProperties
	 * @param exceptionNoticeProperties
	 * @param applicationEventPublisher
	 */
	public ExceptionHandler(PromethreusNoticeProperties noticeProperties,
							ExceptionNoticeProperties exceptionNoticeProperties,
							ApplicationEventPublisher applicationEventPublisher) {
		super();
		this.noticeProperties = noticeProperties;
		this.exceptionNoticeProperties = exceptionNoticeProperties;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * 最基础的异常通知的创建方法
	 *
	 * @param exception 异常信息
	 * @return
	 */
	public ExceptionNotice createNotice(RuntimeException exception) {
		if (containsException(exception)) {
			return null;
		}
		ExceptionNotice exceptionNotice = new ExceptionNotice(exception,
			exceptionNoticeProperties.getIncludedTracePackage(), null, noticeProperties.getProjectEnviroment(),
			String.format("%s的异常通知", noticeProperties.getProjectName()));
		exceptionNotice.setProject(noticeProperties.getProjectName());
		applicationEventPublisher.publishEvent(new ExceptionNoticeEvent(this, exceptionNotice));
		return exceptionNotice;

	}

	private boolean containsException(RuntimeException exception) {
		List<Class<? extends Throwable>> thisEClass = getAllExceptionClazz(exception);
		List<Class<? extends Exception>> list = exceptionNoticeProperties.getExcludeExceptions();
		for (Class<? extends Exception> clazz : list) {
			if (thisEClass.stream().anyMatch(clazz::isAssignableFrom)) {
				return true;
			}
		}
		return false;
	}

	private List<Class<? extends Throwable>> getAllExceptionClazz(RuntimeException exception) {
		List<Class<? extends Throwable>> list = new LinkedList<Class<? extends Throwable>>();
		list.add(exception.getClass());
		Throwable cause = exception.getCause();
		while (cause != null) {
			list.add(cause.getClass());
			cause = cause.getCause();
		}
		return list;
	}

	/**
	 * 反射方式获取方法中出现的异常进行的通知
	 *
	 * @param ex     异常信息
	 * @param method 方法名
	 * @param args   参数信息
	 * @return
	 */
	public ExceptionNotice createNotice(RuntimeException ex, String method, Object[] args) {
		if (containsException(ex)) {
			return null;
		}
		ExceptionNotice exceptionNotice = new ExceptionNotice(ex, exceptionNoticeProperties.getIncludedTracePackage(),
			args, noticeProperties.getProjectEnviroment(),
			String.format("%s的异常通知", noticeProperties.getProjectName()));
		logger.debug("创建异常通知：" + method);
		exceptionNotice.setProject(noticeProperties.getProjectName());
		applicationEventPublisher.publishEvent(new ExceptionNoticeEvent(this, exceptionNotice));
		return exceptionNotice;

	}

	/**
	 * 创建一个http请求异常的通知
	 *
	 * @param exception
	 * @param url
	 * @param param
	 * @param requesBody
	 * @param headers
	 * @return
	 */
	public HttpExceptionNotice createHttpNotice(RuntimeException exception, String url, Map<String, String> param,
                                                String requesBody, Map<String, String> headers) {
		if (containsException(exception)) {
			return null;
		}
		logger.debug("创建异常通知：" + url);
		HttpExceptionNotice exceptionNotice = new HttpExceptionNotice(exception,
			exceptionNoticeProperties.getIncludedTracePackage(), url, param, requesBody, headers,
			noticeProperties.getProjectEnviroment(), String.format("%s的异常通知", noticeProperties.getProjectName()));
		exceptionNotice.setProject(noticeProperties.getProjectName());
		applicationEventPublisher.publishEvent(new ExceptionNoticeEvent(this, exceptionNotice));
		return exceptionNotice;
	}

}
