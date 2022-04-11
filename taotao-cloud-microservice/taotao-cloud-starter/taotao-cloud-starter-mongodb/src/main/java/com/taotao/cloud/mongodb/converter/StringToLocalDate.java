package com.taotao.cloud.mongodb.converter;

import com.taotao.cloud.common.utils.date.DateUtil;
import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToLocalDate implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		return LocalDateTime.parse(source, DateUtil.DATE_FORMATTER);
	}
}
