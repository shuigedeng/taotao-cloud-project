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
package com.taotao.cloud.web.strategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * BusinessHandlerChooser
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:15:43
 */
public class BusinessHandlerChooser {

	/**
	 * businessHandlerMap
	 */
	private Map<HandlerType, BusinessHandler> businessHandlerMap;

	/**
	 * setBusinessHandlerMap
	 *
	 * @param orderHandlers orderHandlers
	 * @since 2021-09-02 22:15:54
	 */
	public void setBusinessHandlerMap(List<BusinessHandler> orderHandlers) {
		// 注入各类型的订单处理类
		businessHandlerMap = orderHandlers
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(
						orderHandler -> AnnotationUtils.findAnnotation(orderHandler.getClass(),
								HandlerType.class),
						v -> v, (v1, v2) -> v1));
	}

	/**
	 * businessHandlerChooser
	 *
	 * @param type   type
	 * @param source source
	 * @param <T>    T
	 * @param <R>    R
	 * @return {@link com.taotao.cloud.web.strategy.BusinessHandler }
	 * @since 2021-09-02 22:15:59
	 */
	public <R, T> BusinessHandler businessHandlerChooser(String type, String source) {
		HandlerType orderHandlerType = new HandlerTypeImpl(type, source);
		return businessHandlerMap.get(orderHandlerType);
	}
}
