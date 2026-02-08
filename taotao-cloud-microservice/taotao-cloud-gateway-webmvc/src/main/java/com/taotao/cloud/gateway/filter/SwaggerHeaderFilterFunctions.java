/*
 * Copyright 2013-present the original author or authors.
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

package com.taotao.cloud.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.common.Configurable;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * @author 决定“我管了之后要做什么手脚”（改请求、加头、鉴权、限流、改响应……）
 * @date 2025/12/15
 * @description 定义包含所有自定义filter的函数式过滤器类, 供filtersupplier类使用暴露到项目中 一个类只能定义一个静态方法，spring cloud gateway只注册一个静态方法
 *
 * 提示：定义的过滤器类按一个类一个静态方法，多增加几个静态方法spring也只会加载第一个静态方法，静态方法一定要加@Shotcut注解，不然启动报错，报错原因分析及解决，将在另一篇关于gateway疑难杂症解决的文章中展开。
 * 过滤器定义（SampleHandlerFilterFunctions.java）:一个类一个静态过滤器方法（方法上一定要加上@Shortcut注解），多加了也没用spring只会加载第一个静态方法
 */
public abstract class SwaggerHeaderFilterFunctions {

	private SwaggerHeaderFilterFunctions() {
	}

	public static class SwaggerHeaderFilterSupplier extends SimpleFilterSupplier {

		public SwaggerHeaderFilterSupplier() {
			super(SwaggerHeaderFilterFunctions.class);
		}
	}

	private static final String HEADER_NAME = "X-Forwarded-Prefix";

	private static final String SWAGGER_URI = "/v3/api-docs";

	@Shortcut
	@Configurable
	public static HandlerFilterFunction<ServerResponse, ServerResponse> swaggerHeader() {

		return ( request, next ) -> {

			String path = request.servletRequest().getRequestURI();
			if (!StringUtils.endsWithIgnoreCase(path, SWAGGER_URI)) {
				return next.handle(request);
			}

			String basePath = path.substring(0, path.lastIndexOf(SWAGGER_URI));
			ServerRequest modified = ServerRequest.from(request)
				.header(HEADER_NAME, basePath).build();

			return next.handle(modified);
		};
	}

}
