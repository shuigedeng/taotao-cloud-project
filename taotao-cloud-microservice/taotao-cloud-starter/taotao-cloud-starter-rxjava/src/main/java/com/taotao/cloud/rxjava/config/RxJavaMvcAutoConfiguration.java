/**
 * Copyright (c) 2015-2016 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.rxjava.config;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.rxjava.mvc.ObservableReturnValueHandler;
import com.taotao.cloud.rxjava.mvc.SingleReturnValueHandler;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The RxJava Spring MVC integration auto configuration.
 *
 * @author Jakub Narloch
 */
@Configuration
@ConditionalOnProperty(value = "rxjava.mvc.enabled", matchIfMissing = true)
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


	@Bean
	public void rxJavaWebMvcConfiguration(WebMvcConfigurer webMvcConfigurer,
		List<HandlerMethodReturnValueHandler> handlers) {
		LogUtil.started(HandlerMethodReturnValueHandler.class, StarterName.RXJAVA_STARTER);

		webMvcConfigurer.addReturnValueHandlers(handlers);
	}

	//@Configuration
	//public static class RxJavaWebConfiguration {
	//
	//    @Autowired
	//    private List<AsyncHandlerMethodReturnValueHandler> handlers = new ArrayList<>();
	//
	//    @Bean
	//    public WebMvcConfigurer rxJavaWebMvcConfiguration() {
	//        return new WebMvcConfigurerAdapter() {
	//            @Override
	//            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	//                if (handlers != null) {
	//                    returnValueHandlers.addAll(handlers);
	//                }
	//            }
	//        };
	//    }
	//}
}
