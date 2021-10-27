/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.log.annotation;

import com.taotao.cloud.log.annotation.ConditionalOnRequestLogType.RequestLogTypeCondition;
import com.taotao.cloud.log.enums.LogTypeEnum;
import com.taotao.cloud.log.properties.RequestLogProperties;
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
 * @version 1.0.0
 * @since 2020/6/3 13:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Conditional(RequestLogTypeCondition.class)
public @interface ConditionalOnRequestLogType {

	LogTypeEnum logType() default LogTypeEnum.LOGGER;


	public static class RequestLogTypeCondition extends SpringBootCondition {

		@Autowired
		private RequestLogProperties properties;

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
			Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(
				ConditionalOnRequestLogType.class.getName());

			assert annotationAttributes != null;
			LogTypeEnum logTypeEnum = (LogTypeEnum) annotationAttributes.get("logType");

			if (Objects.nonNull(properties)) {
				LogTypeEnum[] types = properties.getTypes();
				boolean b = Arrays.stream(types)
					.anyMatch(type -> type.getCode() == logTypeEnum.getCode());
				return new ConditionOutcome(b, "");
			}
			return new ConditionOutcome(false, "");
		}
	}

}
