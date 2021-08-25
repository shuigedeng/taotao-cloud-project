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
package com.taotao.cloud.web.mvc.validator;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.web.mvc.constraints.DateValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 日期校验格式，通过format的参数来校验格式
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:28
 */
public class DateValueValidator implements ConstraintValidator<DateValue, String> {

	private Boolean required;

	private String format;

	@Override
	public void initialize(DateValue constraintAnnotation) {
		this.required = constraintAnnotation.required();
		this.format = constraintAnnotation.format();
	}

	@Override
	public boolean isValid(String dateValue, ConstraintValidatorContext context) {
		if (StrUtil.isEmpty(dateValue)) {
			// 校验是不是必填
			if (required) {
				return false;
			} else {
				return true;
			}
		} else {
			try {
				// 校验日期格式
				DateUtil.parse(dateValue, format);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}
}
