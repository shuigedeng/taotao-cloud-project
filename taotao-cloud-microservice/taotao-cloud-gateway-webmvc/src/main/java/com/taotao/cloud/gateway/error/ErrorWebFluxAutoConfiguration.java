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

package com.taotao.cloud.gateway.error;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.error.ErrorPage;
import org.springframework.boot.web.error.ErrorPageRegistrar;
import org.springframework.boot.web.error.ErrorPageRegistry;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.cloud.gateway.server.mvc.GatewayServerMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义异常处理
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:12
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
public class ErrorWebFluxAutoConfiguration {

	@Configuration
	public class ErrorPageConfig implements ErrorPageRegistrar {

		@Override
		public void registerErrorPages( ErrorPageRegistry registry) {
			// 注册错误页面
			registry.addErrorPages(
				new ErrorPage(HttpStatus.TOO_MANY_REQUESTS, "/error/429"),
				new ErrorPage(HttpStatus.BAD_GATEWAY, "/error/502"),
				new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, "/error/504"),
				new ErrorPage(Exception.class, "/error/500")
			);
		}
	}
//	// 处理所有异常
//	@ExceptionHandler(Exception.class)
//	@ResponseBody
//	public ResponseEntity<ErrorResponse> handleException(
//		HttpServletRequest request,
//		Exception ex) {
//
//		ErrorResponse error = new ErrorResponse();
//		error.setTimestamp(LocalDateTime.now());
//		error.setPath(request.getRequestURI());
//		error.setError(ex.getMessage());
//		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	// 处理网关转发异常
//	@ExceptionHandler(GatewayForwardException.class)
//	@ResponseBody
//	public ResponseEntity<ErrorResponse> handleGatewayException(
//		HttpServletRequest request,
//		GatewayForwardException ex) {
//
//		ErrorResponse error = new ErrorResponse();
//		error.setTimestamp(LocalDateTime.now());
//		error.setPath(request.getRequestURI());
//		error.setError("Service unavailable: " + ex.getTargetService());
//		error.setStatus(HttpStatus.BAD_GATEWAY.value());
//
//		return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
//	}
//
//	// 处理限流异常
//	@ExceptionHandler(RateLimitException.class)
//	@ResponseBody
//	public ResponseEntity<ErrorResponse> handleRateLimitException(
//		HttpServletRequest request,
//		RateLimitException ex) {
//
//		ErrorResponse error = new ErrorResponse();
//		error.setTimestamp(LocalDateTime.now());
//		error.setPath(request.getRequestURI());
//		error.setError("Too many requests");
//		error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//
//		// 添加 Retry-After 头
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Retry-After", "60");
//
//		return new ResponseEntity<>(error, headers, HttpStatus.TOO_MANY_REQUESTS);
//	}
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public HandlerExceptionResolver handlerExceptionResolver() {
//		return new HandlerExceptionResolver() {
//			@Override
//			public @Nullable ModelAndView resolveException( HttpServletRequest request, HttpServletResponse response,
//				@Nullable Object handler, Exception ex ) {
//				   // 判断异常类型
//        if (ex instanceof ServiceUnavailableException) {
//            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
//            writeJsonResponse(response,
//                "{\"error\":\"Service unavailable\"}");
//            return new ModelAndView();
//        }
//
//        if (ex instanceof RateLimitException) {
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            response.setHeader("Retry-After", "60");
//            writeJsonResponse(response,
//                "{\"error\":\"Too many requests\"}");
//            return new ModelAndView();
//        }
//
//        // 默认处理
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        writeJsonResponse(response,
//            "{\"error\":\"Internal server error\"}");
//        return new ModelAndView();
//			}
//		}
//    }
}
