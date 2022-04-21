/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:08:05
 */
public class CustomMbeanRegistrar implements ApplicationContextAware, InitializingBean,
	DisposableBean {

	/**
	 * applicationContext
	 */
	private ConfigurableApplicationContext applicationContext;
	/**
	 * objectName
	 */
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
