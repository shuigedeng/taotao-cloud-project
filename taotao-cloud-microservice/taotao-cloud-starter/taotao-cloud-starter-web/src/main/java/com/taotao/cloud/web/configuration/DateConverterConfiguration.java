package com.taotao.cloud.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 日期格式全局配置
 *
 * @link https://juejin.im/post/5e940626e51d4546f5790979
 * @link https://blog.csdn.net/weixin_44600430/article/details/105512891
 */
@Slf4j
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DateConverterConfiguration {

	@Bean
	@Primary
	public ObjectMapper serializingObjectMapper() {
		ObjectMapper objectMapper;
		objectMapper = Jackson2ObjectMapperBuilder.json()
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.locale(Locale.CHINA)
			.timeZone(TimeZone.getTimeZone("GMT+8"))
			.modules(new MateJavaTimeModule())
			.build();
		return objectMapper;
	}


	public static class MateJavaTimeModule extends SimpleModule {

		public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
		public static final String PATTERN_DATETIME_MINI = "yyyyMMddHHmmss";
		public static final String PATTERN_DATE = "yyyy-MM-dd";
		public static final String PATTERN_TIME = "HH:mm:ss";

		/**
		 * java 8 时间格式化
		 */
		public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter
			.ofPattern(PATTERN_DATETIME);
		public static final DateTimeFormatter DATETIME_MINI_FORMATTER = DateTimeFormatter
			.ofPattern(PATTERN_DATETIME_MINI);
		public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern(PATTERN_DATE);
		public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
			.ofPattern(PATTERN_TIME);

		public MateJavaTimeModule() {
			super(PackageVersion.VERSION);
			this.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DATETIME_FORMATTER));
			this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
			this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
			this.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DATETIME_FORMATTER));
			this.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
			this.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
		}
	}
}
