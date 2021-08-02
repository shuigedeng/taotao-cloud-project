package com.taotao.cloud.encrypt.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.encrypt.annotation.SignEncrypt;
import com.taotao.cloud.encrypt.handler.SignEncryptHandler;
import com.taotao.cloud.encrypt.wrapper.CacheRequestWrapper;
import com.taotao.cloud.encrypt.wrapper.EncryptRequestWrapperFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.text.html.FormSubmitEvent.MethodType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 签名加密拦截器
 *
 */
public class SignEncryptInterceptor implements MethodInterceptor {

	private final String signSecret;
	private final SignEncryptHandler signEncryptHandler;

	public SignEncryptInterceptor(String signSecret,
		SignEncryptHandler signEncryptHandler) {
		this.signSecret = signSecret;
		this.signEncryptHandler = signEncryptHandler;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object proceed = methodInvocation.proceed();
		CacheRequestWrapper request = (CacheRequestWrapper) ((ServletRequestAttributes) RequestContextHolder
			.currentRequestAttributes()).getRequest();
		if (!MethodType.POST.name().equalsIgnoreCase(request.getMethod()) ||
			!EncryptRequestWrapperFactory.contentIsJson(request.getContentType())) {
			return proceed;
		}
		SignEncrypt annotation = methodInvocation.getMethod().getAnnotation(SignEncrypt.class);
		long timeout = annotation.timeout();
		TimeUnit timeUnit = annotation.timeUnit();
		if (((CacheRequestWrapper) request).getBody().length < 1) {
			return proceed;
		}
		Map<Object, Object> jsonMap = new ObjectMapper().readValue(request.getBody(), Map.class);
		return signEncryptHandler.handle(proceed, timeout, timeUnit, signSecret, jsonMap);
	}
}
