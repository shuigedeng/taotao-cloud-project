package com.taotao.cloud.sign.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sign.advice.DecryptRequestBodyAdvice;
import com.taotao.cloud.sign.advice.EncryptResponseBodyAdvice;
import com.taotao.cloud.sign.advice.SignAspect;
import com.taotao.cloud.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.sign.properties.EncryptProperties;
import com.taotao.cloud.sign.properties.SignProperties;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SignConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:44:00
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = SignProperties.PREFIX, name = "enabled", havingValue = "true")
@Import({
	EncryptResponseBodyAdvice.class,
	DecryptRequestBodyAdvice.class,
	SignAspect.class})
@EnableConfigurationProperties({
	EncryptBodyProperties.class,
	EncryptProperties.class,
	SignProperties.class,})
public class SignAutoConfiguration implements WebMvcConfigurer, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(SignAutoConfiguration.class, StarterName.SIGN_STARTER);
	}

	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter() {
			@Override
			protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
				throws IOException {
				if (object instanceof String) {
					Charset charset = this.getDefaultCharset();
					assert charset != null;
					StreamUtils.copy((String) object, charset, outputMessage.getBody());
				} else {
					super.writeInternal(object, type, outputMessage);
				}
			}
		};
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = mappingJackson2HttpMessageConverter();
		LinkedList<MediaType> mediaTypes = new LinkedList<>();
		mediaTypes.add(MediaType.TEXT_HTML);
		mediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(mediaTypes);
		converters.add(new StringHttpMessageConverter());
		converters.add(converter);
	}


}
