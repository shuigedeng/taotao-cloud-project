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

import com.taotao.cloud.web.enums.StatusEnum;
import com.taotao.cloud.web.mvc.constraints.StatusValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验状态，判断是否为 StatusEnum 中的值 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:04:24
 */
public class StatusValueValidator implements ConstraintValidator<StatusValue, String> {

	private Boolean required;

	@Override
	public void initialize(StatusValue constraintAnnotation) {
		this.required = constraintAnnotation.required();
	}

	@Override
	public boolean isValid(String statusValue, ConstraintValidatorContext context) {

		// 如果是必填的
		if (required && statusValue == null) {
			return false;
		}

		// 如果不是必填，为空的话就通过
		if (!required && statusValue == null) {
			return true;
		}

		// 校验值是否是枚举中的值
		StatusEnum statusEnum = StatusEnum.codeToEnum(statusValue);
		return statusEnum != null;
	}
}
