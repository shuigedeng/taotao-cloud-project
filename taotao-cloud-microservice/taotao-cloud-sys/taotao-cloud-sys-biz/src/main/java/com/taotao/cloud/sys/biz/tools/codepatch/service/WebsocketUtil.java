package com.taotao.cloud.sys.biz.tools.codepatch.service;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
 
public class WebsocketUtil {
 
	public static InetSocketAddress getRemoteAddress(Session session) {
		if (session == null) {
			return null;
		}
		Async async = session.getAsyncRemote();
		
		//在Tomcat 8.0.x版本有效
//		InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
		//在Tomcat 8.5以上版本有效
		InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#socketWrapper#socket#sc#remoteAddress");
		return addr;
	}
 
	private static Object getFieldInstance(Object obj, String fieldPath) {
		String[] fields = fieldPath.split("#");
		for (String field : fields) {
			obj = getField(obj, obj.getClass(), field);
			if (obj == null) {
				return null;
			}
		}
 
		return obj;
	}
 
	private static Object getField(Object obj, Class<?> clazz, String fieldName) {
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field field;
				field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field.get(obj);
			} catch (Exception e) {
			}
		}
 
		return null;
	}
 
}
