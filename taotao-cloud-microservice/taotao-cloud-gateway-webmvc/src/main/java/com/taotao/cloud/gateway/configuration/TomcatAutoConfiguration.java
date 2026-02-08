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

package com.taotao.cloud.gateway.configuration;


import com.taotao.boot.common.constant.StarterNameConstants;
import com.taotao.boot.common.utils.log.LogUtils;
import org.apache.coyote.ProtocolHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.tomcat.autoconfigure.servlet.TomcatServletWebServerAutoConfiguration;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Undertow http2 h2c 配置，对 servlet 开启 自动配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:29:52
 */
@Configuration
public class TomcatAutoConfiguration {

	/**
	 * 实例化UndertowServerFactoryCustomizer，解决undertow启动提示warn的问题
	 */
	@Bean
	public TomcatServerFactoryCustomizer tomcatServerFactoryCustomizer() {
		return new TomcatServerFactoryCustomizer();
	}

	/**
	 * 「编程式配置」通过实现WebServerFactoryCustomizer<UndertowServletWebServerFactory>接口来编程式地配置Undertow,可以更加灵活。
	 * 解决undertow警告Buffer pool was not set on WebSocketDeploymentInfo
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 21:30:01
	 */
	public static class TomcatServerFactoryCustomizer
		implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

		@Override
		public void customize( TomcatServletWebServerFactory factory ) {

			//是否开启虚拟线程
			factory.addProtocolHandlerCustomizers(protocolHandler -> {
				ThreadFactory factory1 = Thread.ofVirtual()
					.name("ttc_virtual_")
					//.inheritInheritableThreadLocals(true)
					.factory();
				protocolHandler.setExecutor(Executors.newThreadPerTaskExecutor(factory1));
			});
		}
	}
}
