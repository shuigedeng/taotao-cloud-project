package com.taotao.cloud.monitor.kuding.aop;

import java.util.Arrays;

import com.taotao.cloud.monitor.kuding.anno.ExceptionListener;
import com.taotao.cloud.monitor.kuding.exceptionhandle.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;


@Aspect
public class ExceptionNoticeAop {

	private ExceptionHandler exceptionHandler;

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionNoticeAop(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@AfterThrowing(value = "@within(listener)", throwing = "e", argNames = "joinPoint,listener,e")
	public void exceptionNotice(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	@AfterThrowing(value = "@annotation(listener)", throwing = "e", argNames = "joinPoint,listener,e")
	public void exceptionNoticeWithMethod(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	private void handleException(RuntimeException exception, String methodName, Object[] args) {
		logger.debug("出现异常：" + methodName
				+ String.join(",", Arrays.stream(args).map(Object::toString).toArray(String[]::new)));
		exceptionHandler.createNotice(exception, methodName, args);
	}
}
