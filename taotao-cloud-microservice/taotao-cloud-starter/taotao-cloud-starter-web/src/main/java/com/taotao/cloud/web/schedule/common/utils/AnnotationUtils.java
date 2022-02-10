package com.taotao.cloud.web.schedule.common.utils;


import com.taotao.cloud.web.schedule.common.utils.proxy.Point;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

public class AnnotationUtils {

	public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue)
		throws Exception {
		InvocationHandler handler = Proxy.getInvocationHandler(annotation);
		Field f;
		try {
			f = handler.getClass().getDeclaredField("memberValues");
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		f.setAccessible(true);
		Map<String, Object> memberValues;
		memberValues = (Map<String, Object>) f.get(handler);
		Object oldValue = memberValues.get(key);
		if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
			throw new IllegalArgumentException();
		}
		memberValues.put(key, newValue);
		return oldValue;
	}

	/**
	 * 根据SuperScheduledOrder排序
	 */
	public static void scheduledOrderSort(List<Point> points) {
		if (points == null || points.isEmpty()) {
			return;
		}
		points.sort((o1, o2) -> {
			if (o1.getInteriorOrder() == null && o2.getInteriorOrder() == null) {
				return o2.getOrder().compareTo(o1.getOrder());
			} else if (o1.getInteriorOrder() != null && o2.getInteriorOrder() != null) {
				return o2.getInteriorOrder().compareTo(o1.getInteriorOrder());
			} else if (o1.getInteriorOrder() != null) {
				return -1;
			} else {
				return 1;
			}
		});
	}
}
