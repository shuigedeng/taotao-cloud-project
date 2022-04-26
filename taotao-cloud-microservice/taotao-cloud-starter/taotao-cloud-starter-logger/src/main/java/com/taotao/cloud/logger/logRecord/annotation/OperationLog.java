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
package com.taotao.cloud.logger.logRecord.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:49:50
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OperationLogs.class)
public @interface OperationLog {

	/**
	 * 商业标识
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String bizId();

	/**
	 * 商业类型
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String bizType();

	/**
	 * 味精
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String msg() default "";

	/**
	 * 标签
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String tag() default "operation";

	/**
	 * 额外
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String extra() default "";

	/**
	 * 操作符id
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:50
	 */
	String operatorId() default "";

	/**
	 * 之前执行函数
	 *
	 * @return boolean
	 * @since 2022-04-26 14:49:50
	 */
	boolean executeBeforeFunc() default false;
}
