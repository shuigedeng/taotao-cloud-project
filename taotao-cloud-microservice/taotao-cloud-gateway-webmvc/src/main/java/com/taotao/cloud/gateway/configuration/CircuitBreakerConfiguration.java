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

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 断路器配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/11/23 14:28
 */
@Configuration
public class CircuitBreakerConfiguration {

//	@Bean
//	public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(
//		CircuitBreakerRegistry circuitBreakerRegistry,
//		TimeLimiterRegistry timeLimiterRegistry, ReactiveResilience4jBulkheadProvider bulkheadProvider,
//		Resilience4JConfigurationProperties resilience4JConfigurationProperties ) {
//
//		CircuitBreakerConfig circuitBreakerConfig =
//			CircuitBreakerConfig.custom()
//				// 滑动窗口的类型为时间窗口
//				.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
//				// 时间窗口的大小为60秒
//				.slidingWindowSize(60)
//				// 在单位时间窗口内最少需要5次调用才能开始进行统计计算
//				.minimumNumberOfCalls(5)
//				// 在单位时间窗口内调用失败率达到50%后会启动断路器
//				.failureRateThreshold(50)
//				// 允许断路器自动由打开状态转换为半开状态
//				.enableAutomaticTransitionFromOpenToHalfOpen()
//				// 在半开状态下允许进行正常调用的次数
//				.permittedNumberOfCallsInHalfOpenState(10)
//				// 断路器打开状态转换为半开状态需要等待60秒
//				.waitDurationInOpenState(Duration.ofSeconds(60))
//				// 所有异常都当作失败来处理
//				.recordExceptions(Throwable.class)
//				.build();
//
//		ReactiveResilience4JCircuitBreakerFactory factory =
//			new ReactiveResilience4JCircuitBreakerFactory(
//				circuitBreakerRegistry,
//				timeLimiterRegistry,
//				bulkheadProvider,
//				resilience4JConfigurationProperties);
//
//		factory.configureDefault(
//			id ->
//				new Resilience4JConfigBuilder(id)
//					.timeLimiterConfig(
//						TimeLimiterConfig.custom()
//							.timeoutDuration(Duration.ofMillis(500))
//							.build())
//					.circuitBreakerConfig(circuitBreakerConfig)
//					.build());
//
//		return factory;
//	}
}
