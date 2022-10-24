package com.taotao.cloud.data.mongodb.converter;

import com.taotao.cloud.common.utils.date.DateUtils;
import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToLocalDateTime implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		return LocalDateTime.parse(source, DateUtils.DATETIME_FORMATTER);
	}
}
