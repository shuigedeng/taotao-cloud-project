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
package com.taotao.cloud.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 配置http服务，使其即支持http又支持https服务（https通过配置文件配置）
 *
 * @author dengtao
 * @date 2020/4/29 22:11
 * @since v1.0
 */
@Configuration
public class HttpConfiguration {

	// @Autowired
	// private HttpHandler httpHandler;
	// private WebServer webServer;
	//
	// @Value("${server.http.port}")
	// private Integer httpPort;
	//
	// @PostConstruct
	// public void start() {
	// 	NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(httpPort);
	// 	WebServer webServer = factory.getWebServer(httpHandler);
	// 	webServer.start();
	// }
	//
	// @PreDestroy
	// public void stop() {
	// 	webServer.stop();
	// }

}
