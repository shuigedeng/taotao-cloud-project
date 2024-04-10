package com.taotao.cloud.gateway.skywalking;

import cn.hutool.core.util.ObjectUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
public class SkywalkingUtil {

	/**
	 * tid放入MDC
	 *
	 * @param exchange
	 */
	public static void putTidIntoMdc(ServerWebExchange exchange) {
		putTidIntoMdc(exchange, "tid");
	}

	/**
	 * tid放入MDC
	 *
	 * @param exchange
	 */
	public static void putTidIntoMdc(ServerWebExchange exchange, String key) {
		try {
			Object entrySpanInstance = exchange.getAttributes().get("SKYWALKING_SPAN");
			if (ObjectUtil.isEmpty(entrySpanInstance)) {
				return;
			}
			Class<?> entrySpanClazz = entrySpanInstance.getClass().getSuperclass().getSuperclass();
			Field field = entrySpanClazz.getDeclaredField("owner");
			field.setAccessible(true);
			Object ownerInstance = field.get(entrySpanInstance);
			Class<?> ownerClazz = ownerInstance.getClass();
			Method getTraceId = ownerClazz.getMethod("getReadablePrimaryTraceId");
			String traceId = (String) getTraceId.invoke(ownerInstance);
			MDC.put(key, traceId);
		}
		catch (Exception e) {
			log.error("gateway追踪码获取失败", e);
		}
	}

}
