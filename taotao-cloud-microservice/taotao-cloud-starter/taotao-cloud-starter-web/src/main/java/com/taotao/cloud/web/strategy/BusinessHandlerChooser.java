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
package com.taotao.cloud.web.strategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 业务处理策略选择器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:17
 */
public class BusinessHandlerChooser {

	private Map<HandlerType, BusinessHandler> businessHandlerMap;

	/**
	 * setBusinessHandlerMap
	 *
	 * @param orderHandlers orderHandlers
	 * @author shuigedeng
	 * @since 2021/8/24 23:20
	 */
	public void setBusinessHandlerMap(List<BusinessHandler> orderHandlers) {
		// 注入各类型的订单处理类
		businessHandlerMap = orderHandlers
			.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(orderHandler -> AnnotationUtils.findAnnotation(orderHandler.getClass(), HandlerType.class),
					v -> v, (v1, v2) -> v1));
	}

	/**
	 * businessHandlerChooser
	 *
	 * @param type   type
	 * @param source source
	 * @return com.taotao.cloud.web.strategy.BusinessHandler<R, T>
	 * @author shuigedeng
	 * @since 2021/8/24 23:20
	 */
	public <R, T> BusinessHandler<R, T> businessHandlerChooser(String type, String source) {
		HandlerType orderHandlerType = new HandlerTypeImpl(type, source);
		return businessHandlerMap.get(orderHandlerType);
	}
}
