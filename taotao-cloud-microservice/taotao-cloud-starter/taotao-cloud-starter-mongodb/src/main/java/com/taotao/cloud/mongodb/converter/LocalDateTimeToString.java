package com.taotao.cloud.mongodb.converter;

import com.taotao.cloud.common.utils.date.DateUtil;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class LocalDateTimeToString implements Converter<LocalDateTime, String> {

	@Override
	public String convert(@NotNull LocalDateTime source) {
		return source.format(DateUtil.DATETIME_FORMATTER);
	}
}
