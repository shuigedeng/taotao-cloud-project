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

import com.taotao.cloud.gateway.filter.RequestTimeFilterFunctions;
import com.taotao.cloud.gateway.filter.SwaggerHeaderFilterFunctions;
import com.taotao.cloud.gateway.predicates.TtcRequestPredicates;
import com.taotao.cloud.gateway.properties.ApiProperties;
import com.taotao.cloud.gateway.properties.DynamicRouteProperties;
import com.taotao.cloud.gateway.properties.FilterProperties;
import com.taotao.cloud.gateway.properties.HttpsProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 全局配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:13
 */
@Configuration
@EnableConfigurationProperties({
    DynamicRouteProperties.class,
    ApiProperties.class,
    FilterProperties.class,
    HttpsProperties.class,
    // NacosConfigProperties.class
})
public class GatewayConfiguration {


    @Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }

	@Bean
	public TtcRequestPredicates.TtcPredicateSupplier customPredicateSupplier() {
		return new TtcRequestPredicates.TtcPredicateSupplier();
	}

	@Bean
	public RequestTimeFilterFunctions.RequestTimeFilterSupplier requestTimeFilterSupplier(){
		return new RequestTimeFilterFunctions.RequestTimeFilterSupplier();
	}


	@Bean
	public SwaggerHeaderFilterFunctions.SwaggerHeaderFilterSupplier swaggerHeaderFilterSupplier(){
		return new SwaggerHeaderFilterFunctions.SwaggerHeaderFilterSupplier();
	}
}
