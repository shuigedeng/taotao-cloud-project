package com.taotao.cloud.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taotao.cloud.web.datatype.MateJavaTimeModule;
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
 * @author pangu
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
}
