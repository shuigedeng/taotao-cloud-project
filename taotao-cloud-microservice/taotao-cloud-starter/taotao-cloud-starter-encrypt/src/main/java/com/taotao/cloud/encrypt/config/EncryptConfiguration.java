package com.taotao.cloud.encrypt.config;

import com.taotao.cloud.encrypt.enums.EncryptType;
import com.taotao.cloud.encrypt.exception.EncryptException;
import com.taotao.cloud.encrypt.handler.EncryptHandler;
import com.taotao.cloud.encrypt.handler.impl.AesEncryptHandler;
import com.taotao.cloud.encrypt.handler.impl.Base64EncryptHandler;
import com.taotao.cloud.encrypt.handler.impl.RsaEncryptHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 加密配置
 *
 */
@Configuration
@EnableAutoConfiguration
public class EncryptConfiguration implements ApplicationContextAware, BeanFactoryPostProcessor,
		EnvironmentAware {

	private ApplicationContext applicationContext;
	private Environment environment;

	public EncryptConfiguration() {
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
		GenericBeanDefinition bean = new GenericBeanDefinition();
		EncryptType type = environment.getProperty("encrypt.type", EncryptType.class);
		String secret = environment.getProperty("encrypt.secret", String.class);
		String publicKey = environment.getProperty("encrypt.publicKey", String.class);
		String privateKey = environment.getProperty("encrypt.privateKey", String.class);
		Boolean debug = environment.getProperty("encrypt.debug", boolean.class);
		if (debug != null && debug) {
			return;
		}
		if (type == null) {
			throw new EncryptException("没有定义加密类型(No encryption type is defined)");
		}
		switch (type) {
			case BASE64:
				bean.setBeanClass(Base64EncryptHandler.class);
				bean.setPrimary(true);
				beanFactory.registerBeanDefinition("encryptHandler", bean);
				break;
			case AES:
				if (secret == null || "".equals(secret.trim())) {
					throw new EncryptException("没有定义秘钥(No secret key is defined)");
				}
				bean.setBeanClass(AesEncryptHandler.class);
				bean.getPropertyValues().add("secret", secret);
				bean.setPrimary(true);
				beanFactory.registerBeanDefinition("encryptHandler", bean);
				break;
			case RSA:
				if (publicKey == null || "".equals(publicKey.trim())) {
					throw new EncryptException("没有定义公钥(No publicKey is defined)");
				}
				if (privateKey == null || "".equals(privateKey.trim())) {
					throw new EncryptException("没有定义私钥(No privateKey is defined)");
				}
				bean.setBeanClass(RsaEncryptHandler.class);
				bean.getPropertyValues().add("publicKey", publicKey);
				bean.getPropertyValues().add("privateKey", privateKey);
				bean.setPrimary(true);
				beanFactory.registerBeanDefinition("encryptHandler", bean);
				break;
			case CUSTOM:
				try {
					beanFactory.getBean(EncryptHandler.class);
				} catch (Exception e) {
					throw new EncryptException("没有自定义加密处理器(No custom encryption processor)");
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
