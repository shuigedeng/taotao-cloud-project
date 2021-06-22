package com.taotao.cloud.common.json;


import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_TIME_FORMAT;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.taotao.cloud.common.enums.BaseEnum;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * jackson 自定义序列化 & 反序列化 规则
 *
 * @author zuihou
 */
public class LampJacksonModule extends SimpleModule {

	public LampJacksonModule() {
		super(PackageVersion.VERSION);
		this.addDeserializer(LocalDateTime.class, LampLocalDateTimeDeserializer.INSTANCE);
		this.addDeserializer(LocalDate.class,
			new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		this.addDeserializer(LocalTime.class,
			new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		this.addSerializer(LocalDateTime.class,
			new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
		this.addSerializer(LocalDate.class,
			new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		this.addSerializer(LocalTime.class,
			new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		this.addSerializer(Long.class, ToStringSerializer.instance);
		this.addSerializer(Long.TYPE, ToStringSerializer.instance);
		this.addSerializer(BigInteger.class, ToStringSerializer.instance);
		this.addSerializer(BigDecimal.class, ToStringSerializer.instance);
		this.addSerializer(BaseEnum.class, EnumSerializer.INSTANCE);
	}

}
