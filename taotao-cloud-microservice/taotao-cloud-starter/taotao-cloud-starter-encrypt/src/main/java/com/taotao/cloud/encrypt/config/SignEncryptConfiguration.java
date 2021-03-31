package com.taotao.cloud.encrypt.config;

import com.taotao.cloud.encrypt.annotation.SignEncrypt;
import com.taotao.cloud.encrypt.handler.SignEncryptHandler;
import com.taotao.cloud.encrypt.handler.impl.SignEncryptHandlerImpl;
import com.taotao.cloud.encrypt.interceptor.SignEncryptInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 签名加密配置
 *
 * @author gaoyang
 */
@ConditionalOnExpression(value = "environment.getProperty('encrypt.signSecret')!=null && " +
		"environment.getProperty('encrypt.signSecret').trim()!=''")
public class SignEncryptConfiguration {

	@ConditionalOnMissingBean(SignEncryptHandler.class)
	@Bean
	public SignEncryptHandler sortSignEncryptHandlerDefult() {
		return new SignEncryptHandlerImpl();
	}

	@Autowired
	private SignEncryptHandler signEncryptHandler;


	@Bean
	public DefaultPointcutAdvisor sortSignEncryptAdvisor(@Value("${encrypt.signSecret}") String sortSignSecret) {
		SignEncryptInterceptor interceptor = new SignEncryptInterceptor(sortSignSecret, signEncryptHandler);
		AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, SignEncrypt.class);
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(pointcut);
		advisor.setAdvice(interceptor);
		return advisor;
	}
}
