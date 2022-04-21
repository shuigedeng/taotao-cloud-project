/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.common.utils.convert;

import com.taotao.cloud.common.utils.convert.ConversionService;
import com.taotao.cloud.common.utils.clazz.ClassUtil;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;

/**
 * 基于 spring ConversionService 类型转换
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class ConvertUtil {

	/**
	 * Convenience operation for converting a source object to the specified targetType. {@link
	 * TypeDescriptor#forObject(Object)}.
	 *
	 * @param source     the source object
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null}
	 *                                  but source is not {@code null}
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T convert(@Nullable Object source, Class<T> targetType) {
		if (source == null) {
			return null;
		}
		if (ClassUtil.isAssignableValue(targetType, source)) {
			return (T) source;
		}
		GenericConversionService conversionService = ConversionService.getInstance();
		return conversionService.convert(source, targetType);
	}

	/**
	 * Convenience operation for converting a source object to the specified targetType, where the
	 * target type is a descriptor that provides additional conversion context. {@link
	 * TypeDescriptor#forObject(Object)}.
	 *
	 * @param source     the source object
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null}
	 *                                  but source is not {@code null}
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType,
		TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		GenericConversionService conversionService = ConversionService.getInstance();
		return (T) conversionService.convert(source, sourceType, targetType);
	}

	/**
	 * Convenience operation for converting a source object to the specified targetType, where the
	 * target type is a descriptor that provides additional conversion context. Simply delegates to
	 * {@link #convert(Object, TypeDescriptor, TypeDescriptor)} and encapsulates the construction of
	 * the source type descriptor using {@link TypeDescriptor#forObject(Object)}.
	 *
	 * @param source     the source object
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null}
	 *                                  but source is not {@code null}
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		GenericConversionService conversionService = ConversionService.getInstance();
		return (T) conversionService.convert(source, targetType);
	}

}
