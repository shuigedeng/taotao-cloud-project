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
package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.web.validation.validator.EnumValueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * EnumValue
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:27:04
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
	ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValueValidator.class})
@Repeatable(EnumValue.List.class)
public @interface EnumValue {

	// 默认错误消息
	String message() default "the integer is not one of the enum values";

	// 约束注解在验证时所属的组别
	Class<?>[] groups() default {};

	// 约束注解的有效负载
	Class<? extends Payload>[] payload() default {};

	Class<? extends Enum> value();

	// 同时指定多个时使用
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.PARAMETER})
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {

		EnumValue[] value();
	}
}
