package com.taotao.cloud.data.mybatis.plus.conditions;


import static com.taotao.cloud.common.constant.StrPool.PERCENT;
import static com.taotao.cloud.common.constant.StrPool.UNDERSCORE;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.data.mybatis.plus.conditions.query.LbqWrapper;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.data.mybatis.plus.conditions.update.LbuWrapper;
import com.taotao.cloud.data.mybatis.plus.utils.StrHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Wrappers 工具类， 该方法的主要目的是为了 缩短代码长度
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:38:32
 */
public final class Wraps {

	private Wraps() {
	}

	/**
	 * 获取 QueryWrap&lt;T&gt;
	 *
	 * @param <T> 实体类泛型
	 * @return QueryWrapper&lt;T&gt;
	 */
	public static <T> QueryWrap<T> q() {
		return new QueryWrap<>();
	}

	/**
	 * 获取 QueryWrap&lt;T&gt;
	 *
	 * @param entity 实体类
	 * @param <T>    实体类泛型
	 * @return QueryWrapper&lt;T&gt;
	 */
	public static <T> QueryWrap<T> q(Class<T> entityClass) {
		return new QueryWrap<>(entityClass);
	}

	/**
	 * 获取 HyLambdaQueryWrapper&lt;T&gt;
	 *
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapper&lt;T&gt;
	 */
	public static <T> LbqWrapper<T> lbQ() {
		return new LbqWrapper<>();
	}

	/**
	 * 获取 HyLambdaQueryWrapper&lt;T&gt;
	 *
	 * @param entity 实体类
	 * @param <T>    实体类泛型
	 * @return LambdaQueryWrapper&lt;T&gt;
	 */
	public static <T> LbqWrapper<T> lbQ(T entity) {
		return new LbqWrapper<>(entity);
	}

	/**
	 * 获取 HyLambdaQueryWrapper&lt;T&gt;
	 *
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 */
	public static <T> LbuWrapper<T> lbU() {
		return new LbuWrapper<>();
	}

	/**
	 * 获取 HyLambdaQueryWrapper&lt;T&gt;
	 *
	 * @param entity 实体类
	 * @param <T>    实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 */
	public static <T> LbuWrapper<T> lbU(T entity) {
		return new LbuWrapper<>(entity);
	}


	public static <T> LbqWrapper<T> lbq(Class<T> entityClass, Map<String, Object> extra,
		Class<T> modelClazz) {
		return q(entityClass, extra, modelClazz).lambda();
	}

	public static <T> QueryWrap<T> q(Class<T> entityClass, Map<String, Object> extra, Class<T> modelClazz) {
		QueryWrap<T> wrapper = entityClass != null ? Wraps.q(entityClass) : Wraps.q();

		if (MapUtil.isNotEmpty(extra)) {
			//拼装区间
			for (Map.Entry<String, Object> field : extra.entrySet()) {
				String key = field.getKey();
				Object value = field.getValue();
				if (ObjectUtil.isEmpty(value)) {
					continue;
				}
				if (key.endsWith("_st")) {
					String beanField = StrUtil.subBefore(key, "_st", true);
					wrapper.ge(getDbField(beanField, modelClazz), DateUtil.getStartTime(value.toString()));
				}
				if (key.endsWith("_ed")) {
					String beanField = StrUtil.subBefore(key, "_ed", true);
					wrapper.le(getDbField(beanField, modelClazz),
						DateUtil.getEndTime(value.toString()));
				}
			}
		}
		wrapper.isNotNull("id");
		return wrapper;
	}

	/**
	 * 根据 bean字段 反射出 数据库字段
	 *
	 * @param beanField 字段
	 * @param clazz     类型
	 * @return 数据库字段名
	 */
	public static String getDbField(String beanField, Class<?> clazz) {
		Field field = ReflectUtil.getField(clazz, beanField);
		if (field == null) {
			return StrUtil.EMPTY;
		}
		TableField tf = field.getAnnotation(TableField.class);
		if (tf != null && StrUtil.isNotEmpty(tf.value())) {
			return tf.value();
		}
		return StrUtil.EMPTY;
	}


	/**
	 * 替换 实体对象中类型为String 类型的参数，并将% 和 _ 符号转义
	 *
	 * @param source 源对象
	 * @return 最新源对象
	 */
	public static <T> T replace(Object source) {
		if (source == null) {
			return null;
		}
		Object target = source;

		Class<?> srcClass = source.getClass();
		Field[] fields = ReflectUtil.getFields(srcClass);
		for (Field field : fields) {
			Object classValue = ReflectUtil.getFieldValue(source, field);
			if (classValue == null) {
				continue;
			}
			//final 和 static 字段跳过
			if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			if (replaceByRemoteData(target, field, classValue)) {
				continue;
			}

			if (!(classValue instanceof String)) {
				continue;
			}
			String srcValue = (String) classValue;
			if (srcValue.contains(PERCENT) || srcValue.contains(UNDERSCORE)) {
				String tarValue = StrHelper.keywordConvert(srcValue);
				ReflectUtil.setFieldValue(target, field, tarValue);
			}
		}
		return (T) target;
	}

	private static boolean replaceByRemoteData(Object target, Field field, Object classValue) {
		//if (classValue instanceof RemoteData) {
		//	RemoteData rd = (RemoteData) classValue;
		//	Object key = rd.getKey();
		//	if (ObjectUtil.isEmpty(key)) {
		//		ReflectUtil.setFieldValue(target, field, null);
		//		return true;
		//	}
		//	if (!(key instanceof String)) {
		//		return true;
		//	}
		//	String strKey = (String) key;
		//	if (strKey.contains(PERCENT) || strKey.contains(UNDERSCORE)) {
		//		String tarValue = StrHelper.keywordConvert(strKey);
		//		rd.setKey(tarValue);
		//		ReflectUtil.setFieldValue(target, field, rd);
		//	}
		//	return true;
		//}
		return false;
	}


}
