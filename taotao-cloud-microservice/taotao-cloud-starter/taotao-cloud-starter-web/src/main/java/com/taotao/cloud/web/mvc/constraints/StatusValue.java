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
package com.taotao.cloud.web.mvc.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.taotao.cloud.web.mvc.validator.StatusValueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 状态校验，校验参数的状态是否是 StatusEnum 中的值 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:03:31
 */
@Documented
@Constraint(validatedBy = StatusValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusValue {

	String message() default "不正确的状态标识";

	Class[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否必填
	 * <p>
	 * 如果必填，在校验的时候本字段没值就会报错
	 */
	boolean required() default true;

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RUNTIME)
	@Documented
	@interface List {

		StatusValue[] value();
	}
}
