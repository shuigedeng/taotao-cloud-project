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
package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.date.DateUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 日期校验格式，通过format的参数来校验格式
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:04:15
 */
public class DateValueValidator implements ConstraintValidator<DateValue, String> {

	private String format;

	@Override
	public void initialize(DateValue constraintAnnotation) {
		this.format = constraintAnnotation.format();
	}

	@Override
	public boolean isValid(String dateValue, ConstraintValidatorContext context) {
		try {
			// 校验日期格式
			DateUtil.parseLocalDate(dateValue, format);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
