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
package com.taotao.cloud.core.initializer;

import static com.taotao.cloud.common.base.CoreProperties.SpringApplicationName;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.taotao.cloud.common.base.CoreProperties;
import com.taotao.cloud.common.base.PropertyCache;
import com.taotao.cloud.common.enums.EnvironmentEnum;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;

/**
 * 应用上下文初始化器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/15 10:45
 */
@Order(0)
public class CoreApplicationContextInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext context) {
		if (context instanceof AnnotationConfigApplicationContext) {
//			AnnotationConfigApplicationContext annotationConfigApplicationContext = (AnnotationConfigApplicationContext) context;
//			annotationConfigApplicationContext.register(Config.class);
		} else {
			if (ContextUtil.mainClass == null) {
				ContextUtil.mainClass = deduceMainApplicationClass();
			}
			ContextUtil.setApplicationContext(context);

			ConfigurableEnvironment environment = context.getEnvironment();
			if ("false"
				.equalsIgnoreCase(
					environment.getProperty(CoreProperties.TaoTaoCloudEnabled, "true"))) {
				return;
			}

			setDefaultProperty("nacos.logging.default.config.enabled", "false", "[taotao cloud 环境变量]");

			//环境变量初始化
			String propertyValue = environment.getProperty(CoreProperties.SpringApplicationName);
			String propertyValue2 = environment.getProperty(CoreProperties.TaoTaoCloudEnv, "dev");

			if (!Strings.isEmpty(propertyValue) && !Strings.isEmpty(propertyValue2)) {
				//optimizeJson(environment);
				optimize(environment);

				//LogUtils.info(CoreApplicationContextInitializer.class,CoreProperties.Project,CoreProperties.SpringApplicationName+"="+propertyValue);
				setDefaultProperty(CoreProperties.SpringApplicationName, propertyValue, "");

				LogUtil.info(CoreProperties.TaoTaoCloudEnv + "=" + propertyValue2);

				for (EnvironmentEnum e2 : EnvironmentEnum.values()) {
					if (e2.getEnv().toString().equalsIgnoreCase(propertyValue2)) {
						setDefaultProperty(e2.getServerkey(), e2.getUrl(), "[taotao cloud 环境变量]");
					}
				}
			}

			optimizeLog(environment);
			this.registerContextRefreshEvent();
		}

	}

	private Class<?> deduceMainApplicationClass() {
		try {
			StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
			for (StackTraceElement stackTraceElement : stackTrace) {
				if ("main".equals(stackTraceElement.getMethodName())) {
					return Class.forName(stackTraceElement.getClassName());
				}
			}
		} catch (ClassNotFoundException ex) {
			// Swallow and continue
		}
		return null;
	}

	private void optimizeLog(ConfigurableEnvironment environment) {
		String message = "[日志标准规范]";

		//MQ客户端日志目录
//		setDefaultProperty("rocketmq.client.logRoot", "log", message);

		//文件优化
//		setDefaultProperty(CoreProperties.LoggingFile, "log/app.log", message);
//		setDefaultProperty(CoreProperties.LoggingFileMaxHistory, "3", message);
//		setDefaultProperty(CoreProperties.LoggingFileMaxSize, "10MB", message);

		//日志优化最大
		ILoggerFactory factory = LoggerFactory.getILoggerFactory();
		if (factory instanceof LoggerContext) {
			Logger root = ((LoggerContext) factory).getLogger("ROOT");
			if (root != null) {
				Appender<ILoggingEvent> file = root.getAppender("FILE");
				if (file instanceof RollingFileAppender) {
					RollingPolicy rollingPolicy = ((RollingFileAppender) file).getRollingPolicy();
					if (rollingPolicy instanceof SizeAndTimeBasedRollingPolicy) {
						setDefaultProperty(CoreProperties.TaoTaoCloudLoggingFileTotalSize, "1GB",
							message);

						((SizeAndTimeBasedRollingPolicy) rollingPolicy).setTotalSizeCap(FileSize
							.valueOf(environment
								.getProperty(CoreProperties.TaoTaoCloudLoggingFileTotalSize,
									"1GB")));
					}
				}
			}
		}
	}

	private void optimize(ConfigurableEnvironment environment) {
		/**启动全局优化默认配置*/
		double cpuCount = Runtime.getRuntime().availableProcessors();

		if (ContextUtil.isWeb()) {
			//tomcat 优化 * 核心数
//			setDefaultProperty(CoreProperties.ServerTomcatMaxThreads, ((int) (200 * cpuCount)) + "",
//				"[自动化调优]");
//			setDefaultProperty(CoreProperties.ServerTomcatMaxConnections,
//				((int) (10000 * cpuCount)) + "", "[自动化调优]");
//			setDefaultProperty(CoreProperties.ServerTomcatMinSpaceThreads,
//				((int) (20 * (cpuCount / 2))) + "", "[自动化调优]");

			//tomcat 传输优化
			//setDefaultProperty(CoreProperties.ServeCompressionEnabled, "true", "[自动化调优]");
			//setDefaultProperty(CoreProperties.ServeCompressionMimeTypes, "application/json,application/xml,text/html,text/xml,text/plain", "[自动化调优]");
		}
		//setDefaultProperty("server.tomcat.accept-count","200");
		//setDefaultProperty("server.connection-timeout","");
	}

	void setDefaultProperty(String key, String defaultPropertyValue, String message) {
		PropertyUtil
			.setDefaultInitProperty(CoreApplicationContextInitializer.class,
				PropertyUtil.getProperty(SpringApplicationName),
				key, defaultPropertyValue, message);
	}

	void registerContextRefreshEvent() {
		PropertyCache.Default.listenUpdateCache("通过配置刷新上下文监听", (data) -> {
			if (data != null && data.size() > 0) {
				for (val e : data.entrySet()) {
					if (!PropertyUtil
						.getPropertyCache(CoreProperties.TaoTaoCloudContextRestartEnabled, false)) {
						return;
					}
					if (e.getKey().equalsIgnoreCase(CoreProperties.TaoTaoCloudContextRestartText)) {
						refreshContext();
						return;
					}
				}
			}
		});
	}

	void refreshContext() {
		if (ContextUtil.getApplicationContext() != null) {
			if (ContextUtil.mainClass == null) {
				LogUtil.error(PropertyUtil.getProperty(SpringApplicationName) + " 重启失败",
					new BaseException("检测到重启上下文事件,因无法找到启动类，重启失败!!!"));
				return;
			}

			val context = ContextUtil.getApplicationContext();
			ApplicationArguments args = context.getBean(ApplicationArguments.class);
			val waitTime = new Random(UUID.randomUUID().getMostSignificantBits()).nextInt(
				PropertyUtil
					.getPropertyCache(CoreProperties.TaoTaoCloudContextRestartTimeSpan, 10));

			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(waitTime);
					context.stop();
					context.close();
					ReflectionUtils.findMethod(ContextUtil.mainClass, "main")
						.invoke(null, new Object[]{args.getSourceArgs()});
				} catch (Exception exp) {
					LogUtil.error(PropertyUtil.getProperty(SpringApplicationName) + "重启失败",
						new BaseException(
							"根据启动类" + ContextUtil.mainClass.getName() + "动态启动main失败"));
				}
			});
			thread.setDaemon(false);
			thread.start();
		}
	}
}
