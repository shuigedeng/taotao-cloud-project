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
package com.taotao.cloud.rxjava.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.rxjava.annotation.RxJava;
import com.taotao.cloud.rxjava.mvc.ObservableReturnValueHandler;
import com.taotao.cloud.rxjava.mvc.SingleReturnValueHandler;
import com.taotao.cloud.rxjava.properties.RxJavaProperties;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * RxJavaMvcAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:49:34
 */
@AutoConfiguration
@EnableConfigurationProperties({RxJavaProperties.class})
@ConditionalOnProperty(prefix = RxJavaProperties.PREFIX, name = "enabled", havingValue = "true")
public class RxJavaMvcAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RxJavaMvcAutoConfiguration.class, StarterName.RXJAVA_STARTER);
	}

	@Bean
	@RxJava
	@ConditionalOnMissingBean
	@ConditionalOnClass(Observable.class)
	public ObservableReturnValueHandler observableReturnValueHandler() {
		LogUtil.started(ObservableReturnValueHandler.class, StarterName.RXJAVA_STARTER);
		return new ObservableReturnValueHandler();
	}

	@Bean
	@RxJava
	@ConditionalOnMissingBean
	@ConditionalOnClass(Single.class)
	public SingleReturnValueHandler singleReturnValueHandler() {
		LogUtil.started(SingleReturnValueHandler.class, StarterName.RXJAVA_STARTER);
		return new SingleReturnValueHandler();
	}

	@Configuration
	@ConditionalOnProperty(prefix = RxJavaProperties.PREFIX + ".mvc", name = "enabled", havingValue = "true")
	public static class RxJavaWebConfiguration implements WebMvcConfigurer {

		@Autowired
		private List<AsyncHandlerMethodReturnValueHandler> handlers;

		@Override
		public void addReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
			if (handlers != null) {
				returnValueHandlers.addAll(handlers);
			}
		}
	}
}
