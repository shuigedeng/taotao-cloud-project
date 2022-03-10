package com.taotao.cloud.web.sensitive.desensitize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.web.enums.SensitiveStrategy;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 敏感信息序列化时 过滤
 */
public class SensitiveJsonSerializer extends JsonSerializer<String>
	implements ContextualSerializer, ApplicationContextAware {

	private SensitiveStrategy strategy;

	//系统配置
	private DesensitizeProperties desensitizeProperties;

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
		throws IOException {

		// 字段序列化处理
		gen.writeString(strategy.desensitizer().apply(value));
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
		throws JsonMappingException {

		// 判定是否 需要脱敏处理
		if (desensitization()) {
			//获取敏感枚举
			Sensitive annotation = property.getAnnotation(Sensitive.class);

			//如果有敏感注解，则加入脱敏规则
			if (Objects.nonNull(annotation) && Objects.equals(String.class,
				property.getType().getRawClass())) {
				this.strategy = annotation.strategy();
				return this;
			}
		}
		return prov.findValueSerializer(property.getType(), property);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		desensitizeProperties = applicationContext.getBean(DesensitizeProperties.class);
	}

	/**
	 * 是否需要脱敏处理
	 */
	private boolean desensitization() {

		//当前用户
		SecurityUser securityUser = SecurityUtil.getUser();

		//默认脱敏
		if (securityUser == null) {
			return true;
		}

		if (securityUser.getType() == 2) {
			return desensitizeProperties.getSensitiveLevel() == 2;
		}

		if (securityUser.getType() == 1) {
			return desensitizeProperties.getSensitiveLevel() >= 1;
		}

		return false;
	}
}
