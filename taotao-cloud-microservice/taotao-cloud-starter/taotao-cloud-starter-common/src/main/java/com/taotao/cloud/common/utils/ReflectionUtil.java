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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.exception.BaseException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ReflectionUtils
 *
 * @author shuigedeng
 * @version 1.0
 * @since 2019-07-31 09:54
 **/
public class ReflectionUtil {

	/**
	 * classForName
	 *
	 * @param type 类型
	 * @author shuigedeng
	 * @since 2021/2/25 16:35
	 */
	public static Class<?> classForName(String type) {
		try {
			return Class.forName(type);
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * tryClassForName
	 *
	 * @param type 类型
	 * @author shuigedeng
	 * @since 2021/2/25 16:35
	 */
	public static Class<?> tryClassForName(String type) {
		try {
			return Class.forName(type);
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * 获取类中方法
	 *
	 * @param cls        类
	 * @param methodName 方法名
	 * @return java.lang.reflect.Method
	 * @author shuigedeng
	 * @since 2021/2/25 16:35
	 */
	public static Method findMethod(Class<?> cls, String methodName) {
		Method find = null;
		while (cls != null) {
			for (Method[] methods : new Method[][]{cls.getMethods(), cls.getDeclaredMethods()}) {
				for (Method method : methods) {
					if (method.getName().equalsIgnoreCase(methodName)) {
						find = method;
						break;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	/**
	 * 获取类中方法
	 *
	 * @param cls        类型
	 * @param methodName 方法名
	 * @param argsTypes  参数类型
	 * @return java.lang.reflect.Method
	 * @author shuigedeng
	 * @since 2021/2/25 16:35
	 */
	public static Method findMethod0(Class<?> cls, String methodName, Class<?>... argsTypes)
		throws NoSuchMethodException, SecurityException {
		Method find = null;
		if (cls != null) {
			find = cls.getMethod(methodName, argsTypes);
		}
		return find;
	}

	/**
	 * 调用对象方法
	 *
	 * @param obj          对象
	 * @param methodName   方法名
	 * @param param        参数
	 * @param defaultValue 默认值
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:35
	 */
	public static <T> T tryCallMethod(Object obj, String methodName, Object[] param,
		T defaultValue) {
		try {
			if (obj != null) {
				Method find = findMethod(obj.getClass(), methodName);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.invoke(obj, param);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			return defaultValue;
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param obj        对象
	 * @param methodName 方法名
	 * @param param      参数
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/2/25 16:36
	 */
	public static Object callMethod(Object obj, String methodName, Object[] param) {
		try {
			Method find = findMethod(obj.getClass(), methodName);
			if (find != null) {
				return find.invoke(obj, param);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param clazz      类型
	 * @param methodName 方法名
	 * @param params     参数
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/2/25 16:36
	 */
	public static Object callMethod(Class<?> clazz, String methodName, Object[] params) {
		try {
			Method find = findMethod(clazz, methodName);
			if (find != null) {
				return find.invoke(null, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param clazz      类型
	 * @param methodName 方法名
	 * @param params     参数
	 * @param paramTypes 参数类型
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/2/25 16:36
	 */
	public static Object callMethodWithParams(Class<?> clazz, String methodName, Object[] params,
		Class<?>... paramTypes) {
		try {
			Method find = findMethod0(clazz, methodName, paramTypes);
			if (find != null) {
				return find.invoke(null, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param object     对象
	 * @param methodName 方法名
	 * @param params     参数
	 * @param paramTypes 参数类型
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/2/25 16:36
	 */
	public static Object callMethodWithParams(Object object, String methodName, Object[] params,
		Class<?>... paramTypes) {
		try {
			Method find = findMethod0(object.getClass(), methodName, paramTypes);
			if (find != null) {
				return find.invoke(object, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 获取字段
	 *
	 * @param cls  类型
	 * @param name 字段名称
	 * @return java.lang.reflect.Field
	 * @author shuigedeng
	 * @since 2021/2/25 16:37
	 */
	public static Field findField(Class<?> cls, String name) {
		Field find = null;
		while (cls != null) {
			for (Field[] fields : new Field[][]{cls.getFields(), cls.getDeclaredFields()}) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(name)) {
						find = field;
						return find;
						//break;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	/**
	 * 获取字段值
	 *
	 * @param obj  对象
	 * @param name 字段名称
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:37
	 */
	public static <T> T getFieldValue(Object obj, String name) {
		try {
			Field find = findField(obj.getClass(), name);
			if (find != null) {
				if (!find.isAccessible()) {
					find.setAccessible(true);
				}
				return (T) find.get(obj);
			}
			throw new Exception("未找到字段" + StringUtil.nullToEmpty(name));
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 获取字段值
	 *
	 * @param obj          对象
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:37
	 */
	public static <T> T tryGetFieldValue(Object obj, String name, T defaultValue) {
		try {
			if (obj != null) {
				Field find = findField(obj.getClass(), name);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.get(obj);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          对象全路径
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:37
	 */
	public static <T> T tryGetStaticFieldValue(String cls, String name, T defaultValue) {
		try {
			return tryGetStaticFieldValue(Class.forName(cls), name, defaultValue);
		} catch (Exception exp) {
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          类型
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:37
	 */
	public static <T> T tryGetStaticFieldValue(Class<?> cls, String name, T defaultValue) {
		try {
			if (cls != null) {
				Field find = findField(cls, name);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.get(cls);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			return defaultValue;
		}
	}

	/**
	 * 设置字段
	 *
	 * @param field 字段
	 * @param obj   对象
	 * @param value 值
	 * @author shuigedeng
	 * @since 2021/2/25 16:38
	 */
	public static void setFieldValue(Field field, Object obj, Object value) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(obj, value);
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 获取值
	 *
	 * @param obj  对象
	 * @param path 路径
	 * @param deft deft
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:38
	 */
	public static <T> T tryGetValue(Object obj, String path, T deft) {
		if (obj == null || path == null || path.length() == 0) {
			return deft;
		}
		Object object = obj;
		for (String name : path.split("\\.")) {
			if (object == null) {
				break;
			}
			Object value = tryGetFieldValue(object, name, null);
			if (value == null) {
				object = tryCallMethod(object, name, null, null);
			} else {
				object = value;
			}
		}
		return object == null ? deft : (T) object;
	}

	/**
	 * 获取值
	 *
	 * @param obj  对象
	 * @param path 路径
	 * @return T
	 * @author shuigedeng
	 * @since 2021/2/25 16:38
	 */
	public static <T> T tryGetValue(Object obj, String path) {
		return tryGetValue(obj, path, null);
	}
}
