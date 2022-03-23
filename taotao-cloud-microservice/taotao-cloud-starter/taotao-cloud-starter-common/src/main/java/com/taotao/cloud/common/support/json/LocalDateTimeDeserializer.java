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
package com.taotao.cloud.common.support.json;


import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_EN;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_EN_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_TIME_FORMAT_EN;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_TIME_FORMAT_EN_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_TIME_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_TIME_FORMAT_MATCHES;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 字段类型是LocalDateTime时，可以按照以下6种格式反序列化： 1. yyyy-MM-dd 2. yyyy年MM月dd日 3. yyyy/MM/dd 4. yyyy-MM-dd
 * HH:mm:ss 5. yyyy年MM月dd日HH时mm分ss秒 6. yyyy/MM/dd HH:mm:ss
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:21:26
 */
public class LocalDateTimeDeserializer extends JSR310DateTimeDeserializerBase<LocalDateTime> {

	private static final long serialVersionUID = 1L;

	/**
	 * INSTANCE
	 */
	public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();
	/**
	 * DEFAULT_FORMATTER
	 */
	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	/**
	 * 以下是支持的6种参数格式
	 */
	private static final DateTimeFormatter DEFAULT_DATE_FORMAT_DTF = DateTimeFormatter
		.ofPattern(DEFAULT_DATE_FORMAT);
	private static final DateTimeFormatter DEFAULT_DATE_FORMAT_EN_DTF = DateTimeFormatter
		.ofPattern(DEFAULT_DATE_FORMAT_EN);
	private static final DateTimeFormatter SLASH_DATE_FORMAT_DTF = DateTimeFormatter
		.ofPattern(SLASH_DATE_FORMAT);
	private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT_DTF = DateTimeFormatter
		.ofPattern(DEFAULT_DATE_TIME_FORMAT);
	private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT_EN_DTF = DateTimeFormatter
		.ofPattern(DEFAULT_DATE_TIME_FORMAT_EN);
	private static final DateTimeFormatter SLASH_DATE_TIME_FORMAT_DTF = DateTimeFormatter
		.ofPattern(SLASH_DATE_TIME_FORMAT);

	@Override
	protected JSR310DateTimeDeserializerBase<LocalDateTime> withShape(JsonFormat.Shape shape) {
		return this;
	}

	private LocalDateTimeDeserializer() {
		this(DEFAULT_FORMATTER);
	}

	public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
		super(LocalDateTime.class, formatter);
	}

	protected LocalDateTimeDeserializer(LocalDateTimeDeserializer base, Boolean leniency) {
		super(base, leniency);
	}

	@Override
	protected LocalDateTimeDeserializer withLeniency(Boolean leniency) {
		return new LocalDateTimeDeserializer(this, leniency);
	}

	@Override
	protected JSR310DateTimeDeserializerBase<LocalDateTime> withDateFormat(
		DateTimeFormatter formatter) {
		return new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(formatter);
	}

	/**
	 * convert
	 *
	 * @param source source
	 * @return {@link java.time.LocalDateTime }
	 * @since 2021-09-02 19:22:08
	 */
	private LocalDateTime convert(String source) {
		if (source.matches(DEFAULT_DATE_FORMAT_MATCHES)) {
			return LocalDateTime
				.of(LocalDate.parse(source, DEFAULT_DATE_FORMAT_DTF), LocalTime.MIN);
		}
		if (source.matches(DEFAULT_DATE_FORMAT_EN_MATCHES)) {
			return LocalDateTime
				.of(LocalDate.parse(source, DEFAULT_DATE_FORMAT_EN_DTF), LocalTime.MIN);
		}
		if (source.matches(SLASH_DATE_FORMAT_MATCHES)) {
			return LocalDateTime.of(LocalDate.parse(source, SLASH_DATE_FORMAT_DTF), LocalTime.MIN);
		}
		if (source.matches(DEFAULT_DATE_TIME_FORMAT_MATCHES)) {
			return LocalDateTime.parse(source, DEFAULT_DATE_TIME_FORMAT_DTF);
		}
		if (source.matches(DEFAULT_DATE_TIME_FORMAT_EN_MATCHES)) {
			return LocalDateTime.parse(source, DEFAULT_DATE_TIME_FORMAT_EN_DTF);
		}
		if (source.matches(SLASH_DATE_TIME_FORMAT_MATCHES)) {
			return LocalDateTime.parse(source, SLASH_DATE_TIME_FORMAT_DTF);
		}
		return null;
	}

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
		throws IOException {
		// 字符串
		if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
			String string = parser.getText().trim();
			if (string.length() == 0) {
				return null;
			}

			try {
				if (_formatter == null) {
					return convert(string);
				}
				if (_formatter == DEFAULT_FORMATTER) {
					// JavaScript by default includes time and zone in JSON serialized Dates (UTC/ISO instant format).
					if (string.length() > 10 && string.charAt(10) == 'T') {
						if (string.endsWith("Z")) {
							return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
						} else {
							return LocalDateTime.parse(string, DEFAULT_FORMATTER);
						}
					}
					return convert(string);
				}

				return LocalDateTime.parse(string, this._formatter);
			} catch (DateTimeException e) {
				return _handleDateTimeException(context, e, string);
			}
		}
		// 数组
		if (parser.isExpectedStartArrayToken()) {
			JsonToken t = parser.nextToken();
			if (t == JsonToken.END_ARRAY) {
				return null;
			}
			if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT)
				&& context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
				final LocalDateTime parsed = deserialize(parser, context);
				if (parser.nextToken() != JsonToken.END_ARRAY) {
					handleMissingEndArrayForSingle(parser, context);
				}
				return parsed;
			}
			if (t == JsonToken.VALUE_NUMBER_INT) {
				LocalDateTime result;

				int year = parser.getIntValue();
				int month = parser.nextIntValue(-1);
				int day = parser.nextIntValue(-1);
				int hour = parser.nextIntValue(-1);
				int minute = parser.nextIntValue(-1);

				t = parser.nextToken();
				if (t == JsonToken.END_ARRAY) {
					result = LocalDateTime.of(year, month, day, hour, minute);
				} else {
					int second = parser.getIntValue();
					t = parser.nextToken();
					if (t == JsonToken.END_ARRAY) {
						result = LocalDateTime.of(year, month, day, hour, minute, second);
					} else {
						int partialSecond = parser.getIntValue();
						if (partialSecond < 1_000 &&
							!context.isEnabled(
								DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
							// value is milliseconds, convert it to nanoseconds
							partialSecond *= 1_000_000;
						}
						if (parser.nextToken() != JsonToken.END_ARRAY) {
							throw context
								.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY,
									"Expected array to end");
						}
						result = LocalDateTime
							.of(year, month, day, hour, minute, second, partialSecond);
					}
				}
				return result;
			}
			context.reportInputMismatch(handledType(),
				"Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", t);
		}
		// 数字
		if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
			return Instant.ofEpochMilli(parser.getLongValue()).atZone(ZoneOffset.ofHours(8))
				.toLocalDateTime();
		}
		// 没看懂这个是啥
		if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
			return (LocalDateTime) parser.getEmbeddedObject();
		}

		return _handleUnexpectedToken(context, parser, "当前参数需要数组、字符串、时间戳。");
	}

}
