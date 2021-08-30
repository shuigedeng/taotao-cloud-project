/**
 * Copyright (c) 2015-2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.rxjava.config;

import com.taotao.cloud.rxjava.mvc.ObservableReturnValueHandler;
import com.taotao.cloud.rxjava.mvc.SingleReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.ArrayList;
import java.util.List;

/**
 * The RxJava Spring MVC integration auto configuration.
 *
 * @author Jakub Narloch
 */
@Configuration
@ConditionalOnProperty(value = "rxjava.mvc.enabled", matchIfMissing = true)
public class RxJavaMvcAutoConfiguration {

    @Bean
    @RxJava
    @ConditionalOnMissingBean
    @ConditionalOnClass(Observable.class)
    public ObservableReturnValueHandler observableReturnValueHandler() {
        return new ObservableReturnValueHandler();
    }

    @Bean
    @RxJava
    @ConditionalOnMissingBean
    @ConditionalOnClass(Single.class)
    public SingleReturnValueHandler singleReturnValueHandler() {
        return new SingleReturnValueHandler();
    }

    @Configuration
    public static class RxJavaWebConfiguration {

        @Autowired
        private List<AsyncHandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        @Bean
        public WebMvcConfigurer rxJavaWebMvcConfiguration() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
                    if (handlers != null) {
                        returnValueHandlers.addAll(handlers);
                    }
                }
            };
        }
    }
}
