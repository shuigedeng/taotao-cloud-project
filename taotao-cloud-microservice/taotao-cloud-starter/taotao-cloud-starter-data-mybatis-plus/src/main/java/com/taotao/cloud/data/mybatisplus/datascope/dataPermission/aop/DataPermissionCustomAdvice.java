package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.aop;

import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.annotation.DataPermission;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义Advice 处理数据权限的拦截器 1. 在执行方法前，将 数据权限 注解入栈 2. 在执行方法后，将 数据权限 注解出栈
 */
@DataPermission
public class DataPermissionCustomAdvice implements MethodInterceptor {

	/**
	 * DataPermission 空对象，方法无数据权限注解时，使用DATA_PERMISSION_NULL占位
	 */
	static final DataPermission DATA_PERMISSION_NULL = DataPermissionCustomAdvice.class
		.getAnnotation(DataPermission.class);

	private final Map<MethodClassKey, DataPermission> dataPermissionCache = new ConcurrentHashMap<>();

	public Map<MethodClassKey, DataPermission> getDataPermissionCache() {
		return dataPermissionCache;
	}

	public DataPermissionCustomAdvice() {
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 方法执行前 获取方法上的数据权限注解
		DataPermission dataPermission = this.findAnnotation(methodInvocation);

		if (Objects.nonNull(dataPermission)) {
			// 数据权限注解入栈
			DataPermissionContextHolder.add(dataPermission);
		}

		try {
			// 执行逻辑
			return methodInvocation.proceed();
		} finally {
			if (Objects.nonNull(dataPermission)) {
				// 数据权限注解出栈
				DataPermissionContextHolder.remove();
			}
		}
	}

	private DataPermission findAnnotation(MethodInvocation methodInvocation) {
		Method method = methodInvocation.getMethod();
		Object targetObject = methodInvocation.getThis();
		Class<?> clazz = Objects.nonNull(targetObject) ? targetObject.getClass() : method.getDeclaringClass();
		MethodClassKey methodClassKey = new MethodClassKey(method, clazz);

		// 从缓存中获取数据权限注解
		DataPermission dataPermission = dataPermissionCache.get(methodClassKey);
		if (Objects.nonNull(dataPermission)) {
			return dataPermission != DATA_PERMISSION_NULL ? dataPermission : null;
		}

		// 从方法中获取
		dataPermission = AnnotationUtils.findAnnotation(method, DataPermission.class);

		if (Objects.isNull(dataPermission)) {
			// 从类上获取
			dataPermission = AnnotationUtils.findAnnotation(clazz, DataPermission.class);
		}

		// 添加到缓存中
		dataPermissionCache.put(methodClassKey,
			Objects.nonNull(dataPermission) ? dataPermission : DATA_PERMISSION_NULL);

		return dataPermission;
	}

}
