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
package com.taotao.cloud.ws.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpsConfig
 *
 * @author shuigedeng
 * @since 2021/2/1 下午1:53
 * @version 2022.03
 */
@Configuration
@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "true", matchIfMissing = false)
public class HttpsConfig {

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}


	/**
	 * 让我们的应用支持HTTP是个好想法，但是需要重定向到HTTPS，
	 * 但是不能同时在application.properties中同时配置两个connector， 所以要以编程的方式配置HTTP
	 * connector，然后重定向到HTTPS connector
	 *
	 * @return Connector
	 */
	public Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(7443);
		return connector;
	}

	/**
	 * 创建wss协议接口
	 *
	 * @return
	 */
	@Bean
	public TomcatContextCustomizer tomcatContextCustomizer() {
		System.out.println("TOMCATCONTEXTCUSTOMIZER INITILIZED");
		return new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				context.addServletContainerInitializer(new WsSci(), null);
			}
		};
	}

}
