//package com.taotao.cloud.auth.api.tmp.social.converter;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//import org.springframework.lang.Nullable;
//
//import java.io.IOException;
//
///**
// * 解决微信返回内容是 TEXT_PLAIN 问题
// */
//public class CustomMappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
//
//	@Nullable
//	private String jsonPrefix;
//
//
//	/**
//	 * Construct a new {@link CustomMappingJackson2HttpMessageConverter} using default configuration
//	 * provided by {@link Jackson2ObjectMapperBuilder}.
//	 */
//	public CustomMappingJackson2HttpMessageConverter() {
//		this(Jackson2ObjectMapperBuilder.json().build());
//	}
//
//	/**
//	 * Construct a new {@link CustomMappingJackson2HttpMessageConverter} with a custom {@link ObjectMapper}.
//	 * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
//	 * @see Jackson2ObjectMapperBuilder#json()
//	 */
//	public CustomMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
//		super(objectMapper, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"), MediaType.TEXT_PLAIN, MediaType.TEXT_HTML);
//	}
//
//
//	/**
//	 * Specify a custom prefix to use for this view's JSON output.
//	 * Default is none.
//	 * @see #setPrefixJson
//	 */
//	public void setJsonPrefix(String jsonPrefix) {
//		this.jsonPrefix = jsonPrefix;
//	}
//
//	/**
//	 * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
//	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
//	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
//	 * This prefix should be stripped before parsing the string as JSON.
//	 * @see #setJsonPrefix
//	 */
//	public void setPrefixJson(boolean prefixJson) {
//		this.jsonPrefix = (prefixJson ? ")]}', " : null);
//	}
//
//
//	@Override
//	protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
//		if (this.jsonPrefix != null) {
//			generator.writeRaw(this.jsonPrefix);
//		}
//	}
//
//}
