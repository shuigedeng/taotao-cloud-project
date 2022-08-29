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
package com.taotao.cloud.web.validation.converter;

import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_FORMAT_EN;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_FORMAT_EN_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_TIME_FORMAT_EN;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_TIME_FORMAT_EN_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_DATE_TIME_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_MONTH_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_MONTH_FORMAT_SLASH;
import static com.taotao.cloud.common.utils.date.DateUtils.DEFAULT_YEAR_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.SLASH_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.SLASH_DATE_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtils.SLASH_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtils.SLASH_DATE_TIME_FORMAT_MATCHES;

import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

/**
 * String2DateConverter
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:11:14
 */
public class String2DateConverter extends BaseDateConverter<Date> implements
		Converter<String, Date> {

	/**
	 * FORMAT
	 */
	protected static final Map<String, String> FORMAT = new LinkedHashMap<>(15);

	static {
		FORMAT.put(DEFAULT_YEAR_FORMAT, "^\\d{4}");
		FORMAT.put(DEFAULT_MONTH_FORMAT, "^\\d{4}-\\d{1,2}$");
		FORMAT.put(DEFAULT_DATE_FORMAT, DEFAULT_DATE_FORMAT_MATCHES);
		FORMAT.put("yyyy-MM-dd HH", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}");
		FORMAT.put("yyyy-MM-dd HH:mm", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$");
		FORMAT.put(DEFAULT_DATE_TIME_FORMAT, DEFAULT_DATE_TIME_FORMAT_MATCHES);
		FORMAT.put(DEFAULT_MONTH_FORMAT_SLASH, "^\\d{4}/\\d{1,2}$");
		FORMAT.put(SLASH_DATE_FORMAT, SLASH_DATE_FORMAT_MATCHES);
		FORMAT.put("yyyy/MM/dd HH", "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}");
		FORMAT.put("yyyy/MM/dd HH:mm", "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$");
		FORMAT.put(SLASH_DATE_TIME_FORMAT, SLASH_DATE_TIME_FORMAT_MATCHES);
		FORMAT.put(DEFAULT_DATE_FORMAT_EN, DEFAULT_DATE_FORMAT_EN_MATCHES);
		FORMAT.put(DEFAULT_DATE_TIME_FORMAT_EN, DEFAULT_DATE_TIME_FORMAT_EN_MATCHES);
	}

	/**
	 * 格式化日期
	 *
	 * @param dateStr String 字符型日期
	 * @param format  String 格式
	 * @return {@link java.util.Date }
	 * @author shuigedeng
	 * @since 2021-09-02 22:11:29
	 */
	protected static Date parseDate(String dateStr, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			//严格模式
			dateFormat.setLenient(false);
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			LogUtils.error("转换日期失败, date={}, format={}", dateStr, format, e);
			throw new BaseException(e.getMessage(), e);
		}
	}

	@Override
	protected Map<String, String> getFormat() {
		return FORMAT;
	}

	@Override
	@Nullable
	public Date convert(String source) {
		return super.convert(source, key -> parseDate(source, key));
	}

}
