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
package com.taotao.cloud.dubbo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 允许值
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:22:09
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
// @Constraint(validatedBy = { })
@Constraint(validatedBy = {AllowedValueValidator.class})
public @interface AllowedValue {

	/**
	 * 消息
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:22:10
	 */
	String message() default "参数值不在合法范围内";

	/**
	 * 组
	 *
	 * @return {@link Class }<{@link ? }>{@link [] }
	 * @since 2022-04-27 17:22:10
	 */
	Class<?>[] groups() default { };

	/**
	 * 有效载荷
	 *
	 * @return {@link Class }<{@link ? } {@link extends } {@link Payload }>{@link [] }
	 * @since 2022-04-27 17:22:10
	 */
	Class<? extends Payload>[] payload() default { };

	/**
	 * 价值
	 *
	 * @return {@link long[] }
	 * @since 2022-04-27 17:22:10
	 */
	long[] value() default {};
 
}
