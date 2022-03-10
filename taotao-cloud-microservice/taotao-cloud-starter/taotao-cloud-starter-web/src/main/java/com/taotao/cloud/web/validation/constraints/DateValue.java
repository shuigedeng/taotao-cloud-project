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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.web.validation.constraints.DateValue.List;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 日期格式的校验，根据format参数的格式校验
 * <p>
 * format可以填写：
 * <p>
 * yyyy-MM-dd
 * <p>
 * yyyy-MM-dd HH:mm:ss
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:07:34
 */
@Documented
@Constraint(validatedBy = DateValueValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface DateValue {

	String message() default "日期格式不正确,格式应该为{format}";

	Class[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 日期校验的格式，默认 yyyy-MM-dd
	 */
	String format() default DateUtil.PATTERN_DATE;

	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RUNTIME)
	@Documented
	@interface List {

		DateValue[] value();
	}
}
