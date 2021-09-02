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

import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.exception.BaseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.springframework.boot.convert.ApplicationConversionService;

/**
 * BeanUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:45:40
 */
public class BeanUtil {

	private BeanUtil() {
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @author shuigedeng
	 * @since 2021-09-02 17:45:45
	 */

	public static void copyIgnoredNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreNullValue().ignoreError());
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @author shuigedeng
	 * @since 2021-09-02 17:45:45
	 */
	public static void copyIncludeNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreError());
	}

	/**
	 * convert 类型转换
	 *
	 * @param value 值
	 * @param type  类型
	 * @param <T>   T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 17:46:05
	 */
	public static <T> T convert(Object value, Class<T> type) {
		if (value == null) {
			return null;
		}
		return ApplicationConversionService.getSharedInstance().convert(value, type);
	}

	/**
	 * 类型转换
	 *
	 * @param value 值
	 * @param type  类型
	 * @param <T>   T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 17:46:40
	 */
	public static <T> T tryConvert(Object value, Class<T> type) {
		try {
			return convert(value, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 深度克隆
	 *
	 * @param obj obj
	 * @param <T> T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 17:47:00
	 */
	public static <T> T deepClone(T obj) {
		try {
			try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
				try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
					out.writeObject(obj);
					try (ByteArrayInputStream byteIn = new ByteArrayInputStream(
						byteOut.toByteArray())) {
						ObjectInputStream in = new ObjectInputStream(byteIn);
						return (T) in.readObject();
					}
				}
			}
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}
}
