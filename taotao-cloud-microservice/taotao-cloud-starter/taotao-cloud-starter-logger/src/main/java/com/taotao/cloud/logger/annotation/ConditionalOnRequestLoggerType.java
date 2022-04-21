/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.logger.annotation;

import com.taotao.cloud.logger.annotation.ConditionalOnRequestLoggerType.RequestLogTypeCondition;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 系统操作记录
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/3 13:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Conditional(RequestLogTypeCondition.class)
public @interface ConditionalOnRequestLoggerType {

	RequestLoggerTypeEnum logType() default RequestLoggerTypeEnum.LOGGER;

	public static class RequestLogTypeCondition extends SpringBootCondition {

		@Autowired
		private RequestLoggerProperties properties;

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
			Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(
				ConditionalOnRequestLoggerType.class.getName());

			assert annotationAttributes != null;
			RequestLoggerTypeEnum requestLoggerTypeEnum = (RequestLoggerTypeEnum) annotationAttributes.get("logType");

			if (Objects.nonNull(properties)) {
				RequestLoggerTypeEnum[] types = properties.getTypes();
				boolean b = Arrays.stream(types)
					.anyMatch(type -> type.getCode() == requestLoggerTypeEnum.getCode());
				return new ConditionOutcome(b, "");
			}
			return new ConditionOutcome(false, "");
		}
	}

}
