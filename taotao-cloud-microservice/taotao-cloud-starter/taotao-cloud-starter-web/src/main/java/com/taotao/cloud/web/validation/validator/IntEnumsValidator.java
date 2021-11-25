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
package com.taotao.cloud.web.validation.validator;

import cn.hutool.core.util.ArrayUtil;
import com.taotao.cloud.web.validation.constraints.IntEnums;
import java.util.Arrays;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * IntEnumsValidator 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:04:20
 */
public class IntEnumsValidator implements ConstraintValidator<IntEnums, Integer> {

	private int[] enumList;
	private IntEnums constraintAnnotation;

	@Override
	public void initialize(IntEnums constraintAnnotation) {
		this.enumList = constraintAnnotation.value();
		this.constraintAnnotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
		if (Objects.isNull(value) || ArrayUtil.contains(enumList, value)) {
			return true;
		} else {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(
					String.format("当前值: [%s] 不在字段范围内,字段范围为[%s]", value, Arrays.toString(enumList)))
				.addConstraintViolation();
			return false;
		}
	}
}
