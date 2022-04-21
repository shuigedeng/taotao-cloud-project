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

package com.taotao.cloud.common.utils.convert;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * 类型 转换 服务，添加了 IEnum 转换
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class ConversionService extends DefaultFormattingConversionService {
	@Nullable
	private static volatile ConversionService SHARED_INSTANCE;

	public ConversionService() {
		super();
		super.addConverter(new EnumToStringConverter());
		super.addConverter(new StringToEnumConverter());
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p>
	 * Note: This method actually returns an {@link ConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 *
	 * @return the shared {@code MicaConversionService} instance (never{@code null})
	 */
	public static GenericConversionService getInstance() {
		ConversionService sharedInstance = ConversionService.SHARED_INSTANCE;
		if (sharedInstance == null) {
			synchronized (ConversionService.class) {
				sharedInstance = ConversionService.SHARED_INSTANCE;
				if (sharedInstance == null) {
					sharedInstance = new ConversionService();
					ConversionService.SHARED_INSTANCE = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

}
