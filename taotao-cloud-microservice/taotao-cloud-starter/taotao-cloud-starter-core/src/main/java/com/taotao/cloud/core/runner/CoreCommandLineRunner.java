/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.core.runner;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.properties.CoreProperties;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * CoreCommandLineRunner
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:59:18
 */
public class CoreCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

	private final PropertyCache propertyCache;
	private final CoreProperties coreProperties;
	private ApplicationContext applicationContext;

	public CoreCommandLineRunner(PropertyCache propertyCache, CoreProperties coreProperties) {
		this.propertyCache = propertyCache;
		this.coreProperties = coreProperties;
	}

	@Override
	public void run(String... args) {
		registerContextRefreshEvent();

		String strArgs = String.join("|", args);
		LogUtils.info(PropertyUtils.getProperty(CommonConstant.SPRING_APP_NAME_KEY)
			+ " -- started with arguments length: {}, args: {}", args.length, strArgs);
	}

	/**
	 * registerContextRefreshEvent
	 *
	 * @since 2021-09-02 20:23:36
	 */
	private void registerContextRefreshEvent() {
		propertyCache.listenUpdateCache("通过配置刷新上下文监听", (data) -> {
			if (data != null && data.size() > 0) {

				for (Map.Entry<String, Object> e : data.entrySet()) {
					if (!coreProperties.getContextRestartEnabled()) {
						return;
					}

					if (e.getKey().equalsIgnoreCase(CommonConstant.CONTEXT_RESTART_TEXT)) {
						refreshContext();
						return;
					}
				}
			}
		});
	}

	/**
	 * refreshContext
	 *
	 * @since 2021-09-02 20:23:39
	 */
	private void refreshContext() {
		if (ContextUtils.getApplicationContext() != null) {
			if (ContextUtils.mainClass == null) {
				LogUtils.error(PropertyUtils.getProperty(CommonConstant.SPRING_APP_NAME_KEY)
					+ " 检测到重启上下文事件,因无法找到启动类，重启失败!!!");
				return;
			}

			ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
			ApplicationArguments args = context.getBean(ApplicationArguments.class);

			int waitTime = new Random(UUID.randomUUID().getMostSignificantBits()).nextInt(
				coreProperties.getContextRestartTimespan());

			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(waitTime);
					context.stop();
					context.close();
					ReflectionUtils.findMethod(ContextUtils.mainClass, "main")
						.invoke(null, new Object[]{args.getSourceArgs()});
				} catch (Exception exp) {
					LogUtils.error(PropertyUtils.getProperty(CommonConstant.SPRING_APP_NAME_KEY) + "根据启动类"
						+ ContextUtils.mainClass.getName() + "动态启动main失败");
				}
			});

			thread.setName("taotao-cloud-context-refresh-thread");
			thread.setDaemon(false);
			thread.start();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
