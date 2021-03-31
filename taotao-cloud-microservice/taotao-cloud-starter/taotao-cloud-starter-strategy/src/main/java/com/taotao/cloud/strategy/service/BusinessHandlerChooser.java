package com.taotao.cloud.strategy.service;

import com.taotao.cloud.strategy.annonation.HandlerType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 业务处理策略选择器
 *
 * @date 2020-10-5
 */
public class BusinessHandlerChooser {

	private Map<HandlerType, BusinessHandler> businessHandlerMap;

	public void setBusinessHandlerMap(List<BusinessHandler> orderHandlers) {
		// 注入各类型的订单处理类
		businessHandlerMap = orderHandlers.stream().collect(
			Collectors.toMap(orderHandler -> AnnotationUtils
					.findAnnotation(orderHandler.getClass(), HandlerType.class),
				v -> v, (v1, v2) -> v1));
	}

	public <R, T> BusinessHandler<R, T> businessHandlerChooser(String type, String source) {
		HandlerType orderHandlerType = new HandlerTypeImpl(type, source);
		return businessHandlerMap.get(orderHandlerType);
	}
}
