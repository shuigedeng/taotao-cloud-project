package com.taotao.cloud.web.sign;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * <p>响应体数据处理，防止数据类型为String时再进行JSON数据转换，那么产生最终的结果可能被双引号包含</p>
 *
 * @since 2019年4月10日17:21:25
 */
public class HttpConverterConfig implements WebMvcConfigurer {

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
