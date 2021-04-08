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
package com.taotao.cloud.core.endpoint;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * CustomMbeanRegistrar
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/04/08 15:11
 */
public class CustomMbeanRegistrar implements ApplicationContextAware, InitializingBean,
	DisposableBean {

	private ConfigurableApplicationContext applicationContext;
	private final ObjectName objectName = new ObjectName(
		"com.taotao.cloud.core.endpoint:type=CustomAdmin,name=SystemInfoMBean");

	public CustomMbeanRegistrar() throws MalformedObjectNameException {
	}

	@Override
	public void destroy() throws Exception {
		ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		server.registerMBean(new SystemInfo(), this.objectName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}
