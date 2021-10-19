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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

/**
 * ReflectionUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 15:02:23
 */
public class ReflectionUtil {

	private ReflectionUtil() {
	}

	/**
	 * classForName
	 *
	 * @param type 类型
	 * @return {@link Class }
	 * @author shuigedeng
	 * @since 2021-09-02 15:02:32
	 */
	public static Class<?> classForName(String type) {
		try {
			return Class.forName(type);
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 类型
	 *
	 * @param type 类型
	 * @return {@link Class }
	 * @author shuigedeng
	 * @since 2021-09-02 15:02:39
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
	 * @return {@link Method }
	 * @author shuigedeng
	 * @since 2021-09-02 15:02:47
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
	 * 通过枚举名称获取枚举对象
	 *
	 * @param cls        枚举类
	 * @param methodName 方法名称
	 * @param name       name  枚举名称 区分大小写 必须完全一样
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-15 15:56:01
	 */
	public static Object findEnumObjByName(Class<?> cls, String methodName, String name)
		throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Object[] objects = cls.getEnumConstants();
		Method method = cls.getMethod(methodName);
		for (Object object : objects) {
			Object invoke = method.invoke(object);
			if (invoke.equals(name)) {
				return object;
			}
		}
		return null;
	}


	/**
	 * 获取类中方法
	 *
	 * @param cls        类型
	 * @param methodName 方法名
	 * @param argsTypes  参数类型
	 * @return {@link Method }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:01
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
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:09
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
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param obj        对象
	 * @param methodName 方法名
	 * @param param      参数
	 * @return {@link Object }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:17
	 */
	public static Object callMethod(Object obj, String methodName, Object[] param) {
		try {
			Method find = findMethod(obj.getClass(), methodName);
			if (find != null) {
				return find.invoke(obj, param);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param clazz      类型
	 * @param methodName 方法名
	 * @param params     参数
	 * @return {@link Object }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:25
	 */
	public static Object callMethod(Class<?> clazz, String methodName, Object[] params) {
		try {
			Method find = findMethod(clazz, methodName);
			if (find != null) {
				return find.invoke(null, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
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
	 * @return {@link Object }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:38
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
			LogUtil.error(exp);
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
	 * @return {@link Object }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:48
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
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 获取字段
	 *
	 * @param cls  类型
	 * @param name 字段名称
	 * @return {@link Field }
	 * @author shuigedeng
	 * @since 2021-09-02 15:03:57
	 */
	public static Field findField(Class<?> cls, String name) {
		Field find = null;
		while (cls != null) {
			for (Field[] fields : new Field[][]{cls.getFields(), cls.getDeclaredFields()}) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(name)) {
						find = field;
						return find;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	public static RecordComponent findRecord(Class<?> cls, String name) {
		RecordComponent find = null;
		while (cls != null) {
			RecordComponent[] recordComponents = cls.getRecordComponents();
			if (recordComponents.length != 0) {
				for (RecordComponent recordComponent : recordComponents) {
					if (recordComponent.getName().equalsIgnoreCase(name)) {
						find = recordComponent;
						return find;
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
	 * @param <T>  T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:04
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
			LogUtil.error(e);
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 获取字段值
	 *
	 * @param obj          对象
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:13
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
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          对象全路径
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:21
	 */
	public static <T> T tryGetStaticFieldValue(String cls, String name, T defaultValue) {
		try {
			return tryGetStaticFieldValue(Class.forName(cls), name, defaultValue);
		} catch (Exception exp) {
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          类型
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:28
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
			LogUtil.error(exp);
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
	 * @since 2021-09-02 15:04:36
	 */
	public static void setFieldValue(Field field, Object obj, Object value) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(obj, value);
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 获取值
	 *
	 * @param obj  对象
	 * @param path 路径
	 * @param deft deft
	 * @param <T>  T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:43
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
	 * @param <T>  T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 15:04:53
	 */
	public static <T> T tryGetValue(Object obj, String path) {
		return tryGetValue(obj, path, null);
	}
}
