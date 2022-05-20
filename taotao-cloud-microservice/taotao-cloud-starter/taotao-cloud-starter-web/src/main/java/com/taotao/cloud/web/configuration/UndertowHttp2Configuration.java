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
package com.taotao.cloud.web.configuration;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import io.undertow.Undertow;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

/**
 * Undertow http2 h2c 配置，对 servlet 开启
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:29:52
 */
@AutoConfiguration
@ConditionalOnClass(Undertow.class)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
public class UndertowHttp2Configuration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(UndertowHttp2Configuration.class, StarterName.WEB_STARTER);
	}

	/**
	 * 实例化UndertowServerFactoryCustomizer，解决undertow启动提示warn的问题
	 */
	@Bean
	public UndertowServerFactoryCustomizer undertowServerFactoryCustomizer() {
		LogUtil.started(UndertowServerFactoryCustomizer.class, StarterName.WEB_STARTER);

		return new UndertowServerFactoryCustomizer();
	}

	/**
	 * 解决undertow警告Buffer pool was not set on WebSocketDeploymentInfo
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 21:30:01
	 */
	public static class UndertowServerFactoryCustomizer implements
		WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

		@Override
		public void customize(UndertowServletWebServerFactory factory) {
			UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer = deploymentInfo -> {
				WebSocketDeploymentInfo info = (WebSocketDeploymentInfo) deploymentInfo.getServletContextAttributes()
					.get(WebSocketDeploymentInfo.ATTRIBUTE_NAME);
				XnioWorker worker = getXnioWorker();
				ByteBufferPool buffers = new DefaultByteBufferPool(
					Boolean.getBoolean("io.undertow.websockets.direct-buffers"), 1024, 100, 12);
				info.setWorker(worker);
				info.setBuffers(buffers);
			};
			factory.addDeploymentInfoCustomizers(undertowDeploymentInfoCustomizer);
			factory.addBuilderCustomizers(builder -> builder.setServerOption(ENABLE_HTTP2, true));
		}

		/**
		 * getXnioWorker
		 *
		 * @return {@link XnioWorker }
		 * @since 2021-10-10 09:08:51
		 */
		private XnioWorker getXnioWorker() {
			XnioWorker worker = null;
			try {
				worker = Xnio.getInstance()
					.createWorker(OptionMap.create(Options.THREAD_DAEMON, true));
			} catch (IOException ignored) {
			}
			return worker;
		}
	}
}
