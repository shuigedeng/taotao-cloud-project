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
package com.taotao.cloud.data.mybatis.plus.cipher.interceptor;

import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.data.mybatis.plus.cipher.annotation.Cipher;
import com.taotao.cloud.data.mybatis.plus.cipher.annotation.Encrypted;
import com.taotao.cloud.data.mybatis.plus.cipher.service.CryptService;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 字段加解密拦截器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 13:31:21
 */
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@SuppressWarnings("unchecked")
public class FieldEncryptInterceptor implements Interceptor {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FieldEncryptInterceptor.class);
	/**
	 * 当地原始字段映射
	 */
	private final ThreadLocal<Map<Object, Map<Field, String>>> originalFieldMapLocal = ThreadLocal
		.withInitial(ConcurrentHashMap::new);

	/**
	 * 执行器参数计算4
	 */
	private static final int EXECUTOR_PARAMETER_COUNT_4 = 4;
	/**
	 * 映射语句指数
	 */
	private static final int MAPPED_STATEMENT_INDEX = 0;
	/**
	 * 参数指标
	 */
	private static final int PARAMETER_INDEX = 1;
	/**
	 * 行范围指数
	 */
	private static final int ROW_BOUNDS_INDEX = 2;
	/**
	 * 缓存键索引
	 */
	private static final int CACHE_KEY_INDEX = 4;
	/**
	 * 绑定sql索引
	 */
	private static final int BOUND_SQL_INDEX = 5;
	/**
	 * 选择关键
	 */
	private static final String SELECT_KEY = "selectKey";
	/**
	 * sql id
	 */
	private String sqlId = "";
	/**
	 * 恒定密文
	 */
	private static String CONSTANT_CIPHER_TEXT;
	/**
	 * 恒定值
	 */
	private static final String CONSTANT_VALUE = "0.00";
	/**
	 * 常量值
	 */
	private static final List<String> CONSTANT_VALUES = Arrays.asList("0,", "0.0", "0.00", "0.000");

	/**
	 * 拦截
	 *
	 * @param invocation 调用
	 * @return {@link Object }
	 * @since 2022-07-12 13:31:21
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		Cipher cipher;
		boolean cache = Boolean.FALSE;
		MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
		cipher = getEnableCipher(ms);
		Object parameter = args[PARAMETER_INDEX];
		final Map<Object, Map<Field, String>> originalFieldMap = originalFieldMapLocal.get();
		if (ms.getId().startsWith(sqlId) && ms.getId().endsWith(SELECT_KEY)) {
			restore(parameter);
			originalFieldMap.clear();
		}

		if (Objects.isNull(cipher)) {
			return invocation.proceed();
		} else {
			SqlCommandType sqlCommandType = ms.getSqlCommandType();
			if (SqlCommandType.SELECT.equals(sqlCommandType)) {
				cache = isCache(invocation);
			} else {
				updateParameters(cipher, ms, parameter);
			}

			Object object = invocation.proceed();
			if (!CollectionUtils.isEmpty(originalFieldMap)) {
				restore(parameter);
				originalFieldMap.clear();
			}

			if (!cache) {
				if (SqlCommandType.SELECT.equals(sqlCommandType)) {
					return decrypt(object);
				}
			}
			return object;
		}
	}

	/**
	 * 修改入参信息
	 *
	 * @param cipher 加解密实体
	 * @param ms           MappedStatement
	 * @param parameter    入参参数
	 * @since 2022-07-12 13:31:21
	 */
	private void updateParameters(Cipher cipher, MappedStatement ms, Object parameter) {
		sqlId = ms.getId();
		boolean decrypt = cipher.value().equals(Cipher.CipherType.DECRYPT);
		if (!(parameter instanceof Map)) {
			encryptByAnnByList(Collections.singletonList(parameter), decrypt);
		} else {
			Map<String, Object> map = getParameterMap(parameter);
			map.forEach((k, v) -> {
				if (v instanceof Collection) {
					encryptByAnnByList((List<Object>) v, decrypt);
				} else {
					encryptByAnnByList(Collections.singletonList(v), decrypt);
				}
			});
		}
	}

	/**
	 * 获取参数的map 集合
	 *
	 * @param parameter 参数object
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-07-12 13:31:21
	 */
	private Map<String, Object> getParameterMap(Object parameter) {
		Set<Integer> hashCodeSet = new HashSet<>();
		return ((Map<String, Object>) parameter).entrySet().stream().filter(e -> Objects.nonNull(e.getValue())).filter(r -> {
			if (!hashCodeSet.contains(r.getValue().hashCode())) {
				hashCodeSet.add(r.getValue().hashCode());
				return true;
			}
			return false;
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 恢复原始值，将内存中的密文回刷为明文
	 *
	 * @param obz 操作对象
	 * @since 2022-07-12 13:31:21
	 */
	private void restore(Object obz) {
		final Map<Object, Map<Field, String>> originalFieldMap = originalFieldMapLocal.get();
		if (!(obz instanceof Map)) {
			Map<Field, String> ori = originalFieldMap.get(obz);
			if (Objects.nonNull(ori)) {
				ori.forEach((field, k) -> ReflectionUtils.setField(field, obz, k));
			}
		} else {
			Map<String, Object> map = getParameterMap(obz);
			map.forEach((k, v) -> {
				if (v instanceof Collection) {
					((List<Object>) v).stream().filter(Objects::nonNull).forEach(
						obj -> {
							Map<Field, String> ori = originalFieldMap.get(obj);
							if (Objects.nonNull(ori)) {
								ori.forEach((field, value) -> ReflectionUtils.setField(field, obj, value));
							}
						});
				} else {
					Map<Field, String> ori = originalFieldMap.get(v);
					if (Objects.nonNull(ori)) {
						ori.forEach((field, value) -> ReflectionUtils.setField(field, v, value));
					}
				}
			});
		}

	}

	/**
	 * 判断是否有缓存信息
	 *
	 * @param invocation 调用链
	 * @return boolean
	 * @since 2022-07-12 13:31:21
	 */
	private boolean isCache(Invocation invocation) throws IllegalAccessException {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
		Object parameter = args[PARAMETER_INDEX];
		BoundSql bs;
		CacheKey cacheKey = null;
		if (args.length == EXECUTOR_PARAMETER_COUNT_4) {
			//4 个参数时
			bs = ms.getBoundSql(parameter);
		} else {
			//6 个参数时
			cacheKey = (CacheKey) args[CACHE_KEY_INDEX];
			bs = (BoundSql) args[BOUND_SQL_INDEX];
		}
		Object executor = invocation.getTarget();
		Executor baseExecutor;
		if (executor instanceof CachingExecutor) {
			Field field = ReflectionUtils.findField(CachingExecutor.class, "delegate");
			assert field != null;
			field.setAccessible(true);
			baseExecutor = (Executor) field.get(executor);
		} else {
			baseExecutor = (BaseExecutor) invocation.getTarget();
		}
		if (Objects.isNull(cacheKey)) {
			cacheKey = baseExecutor.createCacheKey(ms, parameter, (RowBounds) args[ROW_BOUNDS_INDEX], bs);
		}
		Field field = ReflectionUtils.findField(BaseExecutor.class, "localCache");
		assert field != null;
		field.setAccessible(true);
		PerpetualCache localCache = (PerpetualCache) field.get(baseExecutor);
		return Objects.nonNull(localCache.getObject(cacheKey));
	}

	/**
	 * 解密
	 *
	 * @param object 需要解密的数据源
	 * @return {@link Object }
	 * @since 2022-07-12 13:31:21
	 */
	private Object decrypt(Object object) {
		if (Objects.nonNull(object)) {
			if (object instanceof List) {
				encryptByAnnByList((List<Object>) object, true);
			} else {
				encryptByAnnByList(Collections.singletonList(object), true);
			}
		}
		return object;
	}

	/**
	 * 得到启用密码
	 *
	 * @param ms 女士
	 * @return {@link Cipher }
	 * @since 2022-07-12 13:31:21
	 */
	private Cipher getEnableCipher(MappedStatement ms) {
		String namespace = ms.getId();
		String className = namespace.substring(0, namespace.lastIndexOf("."));
		String methodName = ms.getId().substring(ms.getId().lastIndexOf(".") + 1);
		Method[] mes;
		try {
			mes = Class.forName(className).getMethods();
			for (Method m : mes) {
				if (m.getName().equals(methodName)) {
					if (m.isAnnotationPresent(Cipher.class)) {
						return m.getAnnotation(Cipher.class);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 批量加解密操作
	 *
	 * @param list    需要加解密的实体集合
	 * @param decrypt 是否解密，true 解密操作，false 加密操作
	 * @since 2022-07-12 13:31:21
	 */
	private void encryptByAnnByList(List<Object> list, boolean decrypt) {
		//获取字段上的注解值
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		Set<Field> annotationFields = this.getFields(list.get(0)).stream().filter(field -> field.isAnnotationPresent(Encrypted.class))
			.collect(Collectors.toSet());
		List<String> strings = list.stream().flatMap((obj) -> getFields(obj).stream()
				.filter(annotationFields::contains)
				.map(field -> this.getField(field, obj)))
			.filter(Objects::nonNull)
			.map(Object::toString)
			.toList();

		Map<String, String> map = IntStream.range(0, strings.size()).mapToObj(i -> new Tuple(i, strings.get(i)))
			.collect(Collectors.groupingBy(t -> t.index / 1000))
			.values().stream()
			.collect(HashMap::new,
				(a, b) -> a.putAll(batchCipher(b, decrypt)),
				HashMap::putAll
			);

		list.forEach(obj -> {
			Map<Field, String> fieldOriMap = new HashMap<>();

			getFields(obj).stream()
				.filter(annotationFields::contains)
				.forEach(field -> {
					Object value = this.getField(field, obj);
					if (!decrypt) {
						fieldOriMap.put(field, String.valueOf(value));
					}
					if (Objects.nonNull(value)) {
						ReflectionUtils.setField(field, obj, getConversionService().convert(map.get(value.toString()), field.getType()));
					}
				});

			if (!decrypt) {
				originalFieldMapLocal.get().put(obj, fieldOriMap);
			}
		});
	}

	/**
	 * 元组
	 *
	 * @author shuigedeng
	 * @version 2022.07
	 * @since 2022-07-12 13:31:24
	 */
	static class Tuple {
		/**
		 * 指数
		 */
		int index;
		/**
		 * 价值
		 */
		String value;

		/**
		 * 元组
		 *
		 * @param index 指数
		 * @param value 价值
		 * @since 2022-07-12 13:31:24
		 */
		Tuple(int index, String value) {
			this.index = index;
			this.value = value;
		}
	}


	/**
	 * 获取字段
	 *
	 * @param obj obj
	 * @return {@link List }<{@link Field }>
	 * @since 2022-07-12 13:31:22
	 */
	private List<Field> getFields(Object obj) {
		List<Field> fieldList = new ArrayList<>();
		Class<?> tempClass = obj.getClass();
		//当父类为null的时候说明到达了最上层的父类(Object类).
		while (tempClass != null) {
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			//得到父类,然后赋给自己
			tempClass = tempClass.getSuperclass();
		}
		return fieldList;
	}


	/**
	 * 获取字段
	 *
	 * @param field 场
	 * @param obj   obj
	 * @return {@link Object }
	 * @since 2022-07-12 13:31:22
	 */
	private Object getField(Field field, Object obj) {
		ReflectionUtils.makeAccessible(field);
		return ReflectionUtils.getField(field, obj);
	}

	/**
	 * 批量实现待加解密操作
	 *
	 * @param oriValues 原始字段
	 * @param decrypt   true 解密 false 加密
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-07-12 13:31:22
	 */
	private Map<String, String> batchCipher(List<Tuple> oriValues, boolean decrypt) {
		Map<String, String> result = new HashMap<>();
		if (decrypt) {
			List<String> result2 = oriValues.stream().filter(tuple -> {
				if (Objects.equals(tuple.value, CONSTANT_CIPHER_TEXT)) {
					result.put(tuple.value, CONSTANT_VALUE);
					return false;
				} else {
					return true;
				}
			}).map(tuple -> tuple.value).collect(Collectors.toList());
			if (CollectionUtils.isEmpty(result2)) {
				return result;
			} else {
				result.putAll(this.getCryptService().batchDecrypt(result2));
			}

		} else {
			List<String> result2 = oriValues.stream().filter(tuple -> {
					if (CONSTANT_VALUES.contains(tuple.value)) {
						result.put(tuple.value, CONSTANT_CIPHER_TEXT);
						return false;
					} else {
						return true;
					}
				}).map(tuple -> tuple.value)
				.toList();

			if (CollectionUtils.isEmpty(result2)) {
				return result;
			} else {
				result.putAll(this.getCryptService().batchEncrypt(result2));
			}
		}
		return result;
	}

	/**
	 * 插件
	 *
	 * @param target 目标
	 * @return {@link Object }
	 * @since 2022-07-12 13:31:22
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置属性
	 *
	 * @param properties 属性
	 * @since 2022-07-12 13:31:22
	 */
	@Override
	public void setProperties(Properties properties) {
		CONSTANT_CIPHER_TEXT = properties.getProperty("CONSTANT_CIPHER_TEXT");
		if (!StringUtils.hasLength(CONSTANT_CIPHER_TEXT)) {
			CONSTANT_CIPHER_TEXT = "taotao-cloud";
			// LOGGER.warn("=================================================================================================================================\n" +
			// 	"=====================================当前拦截器未配置常量密文值 key:CONSTANT_CIPHER_TEXT======================================\n" +
			// 	"=====================================为了避免出现异常请在mybatis-config.xml 中设置常量密文key 和值=====================================\n" +
			// 	"=================================================================================================================================");
		}
	}

	/**
	 * 地下室服务
	 * 解加密服务声明
	 */
	private CryptService cryptService;

	/**
	 * 获取解加密服务实现类
	 *
	 * @return {@link CryptService }
	 * @since 2022-07-12 13:31:22
	 */
	private CryptService getCryptService() {
		if (cryptService == null) {
			try {
				cryptService = ContextUtil.getBean(CryptService.class, true);
			} catch (Exception e) {
				LOGGER.error("CryptService not found", e);
				throw new RuntimeException("CryptService not found");
			}
		}
		return cryptService;
	}

	/**
	 * 转换服务
	 * 解加密服务声明
	 */
	private ConversionService conversionService;

	/**
	 * 获取解加密服务实现类
	 *
	 * @return {@link ConversionService }
	 * @since 2022-07-12 13:31:22
	 */
	private ConversionService getConversionService() {
		if (conversionService == null) {
			try {
				conversionService = ContextUtil.getTypeConverter();
			} catch (Exception e) {
				LOGGER.error("TypeConverter not found", e);
				throw new RuntimeException("TypeConverter not found");
			}
		}
		return conversionService;
	}

}
