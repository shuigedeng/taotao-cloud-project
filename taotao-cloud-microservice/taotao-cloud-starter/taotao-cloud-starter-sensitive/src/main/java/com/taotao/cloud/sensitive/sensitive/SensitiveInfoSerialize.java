package com.taotao.cloud.web.sensitive.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.util.Objects;

/**
 * SensitiveInfoSerialize
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-16 14:00:53
 */
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

	private SensitiveType type;

	public SensitiveInfoSerialize() {
	}

	public SensitiveInfoSerialize(final SensitiveType type) {
		this.type = type;
	}

	@Override
	public void serialize(final String s, final JsonGenerator jsonGenerator,
		final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		switch (this.type) {
			case CHINESE_NAME: {
				jsonGenerator.writeString(SensitiveInfoUtils.chineseName(s));
				break;
			}
			case ID_CARD: {
				jsonGenerator.writeString(SensitiveInfoUtils.idCardNum(s));
				break;
			}
			case FIXED_PHONE: {
				jsonGenerator.writeString(SensitiveInfoUtils.fixedPhone(s));
				break;
			}
			case MOBILE_PHONE: {
				jsonGenerator.writeString(SensitiveInfoUtils.mobilePhone(s));
				break;
			}
			case ADDRESS: {
				jsonGenerator.writeString(SensitiveInfoUtils.address(s, 4));
				break;
			}
			case EMAIL: {
				jsonGenerator.writeString(SensitiveInfoUtils.email(s));
				break;
			}
			case BANK_CARD: {
				jsonGenerator.writeString(SensitiveInfoUtils.bankCard(s));
				break;
			}
			case CNAPS_CODE: {
				jsonGenerator.writeString(SensitiveInfoUtils.cnapsCode(s));
				break;
			}
		}

	}

	@Override
	public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
		final BeanProperty beanProperty) throws JsonMappingException {
		// 为空直接跳过
		if (beanProperty != null) {
			// 非 String 类直接跳过
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
				if (sensitiveInfo == null) {
					sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfo.class);
				}
				// 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
				if (sensitiveInfo != null) {
					return new SensitiveInfoSerialize(sensitiveInfo.value());
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(beanProperty);
	}
}
