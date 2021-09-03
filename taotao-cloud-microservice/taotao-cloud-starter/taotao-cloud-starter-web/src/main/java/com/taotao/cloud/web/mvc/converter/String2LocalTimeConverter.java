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
package com.taotao.cloud.web.mvc.converter;

import static com.taotao.cloud.common.utils.DateUtil.DEFAULT_TIME_FORMAT;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

/**
 * String2LocalTimeConverter
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:12:04
 */
public class String2LocalTimeConverter extends BaseDateConverter<LocalTime> implements
	Converter<String, LocalTime> {

	/**
	 * FORMAT
	 */
	protected static final Map<String, String> FORMAT = new LinkedHashMap(5);

	static {
		FORMAT.put(DEFAULT_TIME_FORMAT, "^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
	}

	@Override
	protected Map<String, String> getFormat() {
		return FORMAT;
	}

	@Override
	public LocalTime convert(String source) {
		return super
			.convert(source, (key) -> LocalTime.parse(source, DateTimeFormatter.ofPattern(key)));
	}
}
