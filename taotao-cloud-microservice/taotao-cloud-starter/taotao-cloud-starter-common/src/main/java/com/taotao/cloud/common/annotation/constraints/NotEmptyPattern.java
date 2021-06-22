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
package com.taotao.cloud.common.annotation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 表单字段验证：正则表达式
 * <p>
 * 跟 javax.validation.constraints.Pattern 的区别在于： 本类校验时，传递过来的参数为null或者""时，不会校验正则表达式
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 16:59
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(NotEmptyPattern.List.class)
@Documented
@Constraint(validatedBy = {})
public @interface NotEmptyPattern {

	/**
	 * @return the regular expression to match
	 */
	String regexp();

	/**
	 * @return array of {@code Flag}s considered when resolving the regular expression
	 */
	Flag[] flags() default {};

	/**
	 * @return the error message template
	 */
	String message() default "{javax.validation.constraints.Pattern.message}";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default {};

	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default {};

	/**
	 * Possible Regexp flags.
	 */
	enum Flag {

		/**
		 * Enables Unix lines mode.
		 *
		 * @see java.util.regex.Pattern#UNIX_LINES
		 */
		UNIX_LINES(java.util.regex.Pattern.UNIX_LINES),

		/**
		 * Enables case-insensitive matching.
		 *
		 * @see java.util.regex.Pattern#CASE_INSENSITIVE
		 */
		CASE_INSENSITIVE(java.util.regex.Pattern.CASE_INSENSITIVE),

		/**
		 * Permits whitespace and comments in pattern.
		 *
		 * @see java.util.regex.Pattern#COMMENTS
		 */
		COMMENTS(java.util.regex.Pattern.COMMENTS),

		/**
		 * Enables multiline mode.
		 *
		 * @see java.util.regex.Pattern#MULTILINE
		 */
		MULTILINE(java.util.regex.Pattern.MULTILINE),

		/**
		 * Enables dotall mode.
		 *
		 * @see java.util.regex.Pattern#DOTALL
		 */
		DOTALL(java.util.regex.Pattern.DOTALL),

		/**
		 * Enables Unicode-aware case folding.
		 *
		 * @see java.util.regex.Pattern#UNICODE_CASE
		 */
		UNICODE_CASE(java.util.regex.Pattern.UNICODE_CASE),

		/**
		 * Enables canonical equivalence.
		 *
		 * @see java.util.regex.Pattern#CANON_EQ
		 */
		CANON_EQ(java.util.regex.Pattern.CANON_EQ);

		//JDK flag value
		private final int value;

		private Flag(int value) {
			this.value = value;
		}

		/**
		 * @return flag value as defined in {@link java.util.regex.Pattern}
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * Defines several {@link NotEmptyPattern} annotations on the same element.
	 *
	 * @see NotEmptyPattern
	 */
	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RUNTIME)
	@Documented
	@interface List {

		NotEmptyPattern[] value();
	}
}
