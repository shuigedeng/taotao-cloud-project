package com.taotao.cloud.schedule.utils;


import com.taotao.cloud.schedule.model.entity.Task;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具
 */
public class JobInvokeUtil {
	/**
	 * 执行方法
	 *
	 * @param task 系统任务
	 */
	public static void invokeMethod(Task task) throws Exception {
		String invokeTarget = task.getInvokeTarget();
		String beanName = getBeanName(invokeTarget);
		String methodName = getMethodName(invokeTarget);
		List<Object[]> methodParams = getMethodParams(invokeTarget);

		Object bean = Class.forName(beanName).newInstance();
		invokeMethod(bean, methodName, methodParams);
	}

	/**
	 * 调用任务方法
	 *
	 * @param bean         目标对象
	 * @param methodName   方法名称
	 * @param methodParams 方法参数
	 */
	private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
		throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException {
		if (methodParams != null && methodParams.size() > 0) {
			Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
			method.invoke(bean, getMethodParamsValue(methodParams));
		} else {
			Method method = bean.getClass().getDeclaredMethod(methodName);
			method.invoke(bean);
		}
	}


	/**
	 * 获取bean名称
	 *
	 * @param invokeTarget 目标字符串
	 * @return bean名称
	 */
	public static String getBeanName(String invokeTarget) {
		String beanName = StringUtils.substringBefore(invokeTarget, "(");
		return StringUtils.substringBeforeLast(beanName, ".");
	}

	/**
	 * 获取bean方法
	 *
	 * @param invokeTarget 目标字符串
	 * @return method方法
	 */
	public static String getMethodName(String invokeTarget) {
		String methodName = StringUtils.substringBefore(invokeTarget, "(");
		return StringUtils.substringAfterLast(methodName, ".");
	}

	/**
	 * 获取method方法参数相关列表
	 *
	 * @param invokeTarget 目标字符串
	 * @return method方法相关参数列表
	 */
	public static List<Object[]> getMethodParams(String invokeTarget) {
		List<Object[]> classs = new LinkedList<>();
		String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
		if (StringUtils.isEmpty(methodStr)) {
			return classs;
		}
		String[] methodParams = methodStr.split(",");
		for (String methodParam : methodParams) {
			String str = StringUtils.trimToEmpty(methodParam);
			// String字符串类型，包含'
			if (StringUtils.contains(str, "'")) {
				classs.add(new Object[]{StringUtils.replace(str, "'", ""), String.class});
			}
			// boolean布尔类型，等于true或者false
			else if (StringUtils.equals(str, "true") || StringUtils.equalsIgnoreCase(str, "false")) {
				classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
			}
			// long长整形，包含L
			else if (StringUtils.containsIgnoreCase(str, "L")) {
				classs.add(new Object[]{Long.valueOf(StringUtils.replace(str, "L", "")), Long.class});
			}
			// double浮点类型，包含D
			else if (StringUtils.containsIgnoreCase(str, "D")) {
				classs.add(new Object[]{Double.valueOf(StringUtils.replace(str, "D", "")), Double.class});
			}
			// 其他类型归类为整形
			else {
				classs.add(new Object[]{Integer.valueOf(str), Integer.class});
			}
		}
		return classs;
	}

	/**
	 * 获取参数类型
	 *
	 * @param methodParams 参数相关列表
	 * @return 参数类型列表
	 */
	public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
		Class<?>[] classs = new Class<?>[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Class<?>) os[1];
			index++;
		}
		return classs;
	}

	/**
	 * 获取参数值
	 *
	 * @param methodParams 参数相关列表
	 * @return 参数值列表
	 */
	public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
		Object[] classs = new Object[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Object) os[0];
			index++;
		}
		return classs;
	}
}
