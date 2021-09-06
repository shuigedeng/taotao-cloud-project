/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.disruptor.context;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * DisruptorEventAwareProcessor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:01:56
 */
public class DisruptorEventAwareProcessor implements ApplicationContextAware, BeanPostProcessor,
	InitializingBean {

	/**
	 * disruptorContext
	 */
	private DisruptorApplicationContext disruptorContext;
	/**
	 * applicationContext
	 */
	private ApplicationContext applicationContext;
	/**
	 * Security context used when running with a SecurityManager
	 */
	private SecurityContextProvider securityContextProvider;

	/**
	 * Set the security context provider for this bean factory. If a security manager is set,
	 * interaction with the user code will be executed using the privileged of the provided security
	 * context.
	 *
	 * @param securityProvider {@link SecurityContextProvider} instance
	 */
	public void setSecurityContextProvider(SecurityContextProvider securityProvider) {
		this.securityContextProvider = securityProvider;
	}

	/**
	 * Delegate the creation of the access control context to the {@link #setSecurityContextProvider
	 * SecurityContextProvider}.
	 *
	 * @return {@link AccessControlContext} instance
	 */
	public AccessControlContext getAccessControlContext() {
		if (this.securityContextProvider != null) {
			return this.securityContextProvider.getAccessControlContext();
		}

		if (this.disruptorContext.getApplicationContext()
			.getAutowireCapableBeanFactory() instanceof ConfigurableBeanFactory) {
			ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory) this.disruptorContext.getApplicationContext()
				.getAutowireCapableBeanFactory();
			return beanFactory.getAccessControlContext();
		}

		return AccessController.getContext();
	}

	/**
	 * Create a new ApplicationContextAwareProcessor for the given context.
	 */
	public DisruptorEventAwareProcessor() {
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, String beanName)
		throws BeansException {
		AccessControlContext acc = null;
		if (System.getSecurityManager() != null && (bean instanceof DisruptorEventPublisherAware)) {
			acc = getAccessControlContext();
		}
		if (acc != null) {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					invokeAwareInterfaces(bean);
					return null;
				}
			}, acc);
		} else {
			invokeAwareInterfaces(bean);
		}

		return bean;
	}

	protected void invokeAwareInterfaces(Object bean) {
		if (bean instanceof Aware) {
			//扩展 DisruptorEventPublisherAware
			if (bean instanceof DisruptorEventPublisherAware) {
				DisruptorEventPublisherAware awareBean = (DisruptorEventPublisherAware) bean;
				awareBean.setDisruptorEventPublisher(this.disruptorContext);
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		disruptorContext = new DisruptorApplicationContext();
		disruptorContext.setApplicationContext(applicationContext);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
