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

import com.taotao.cloud.web.validation.constraints.LimitedValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * LimitedValueValidator 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:04:23
 */
public class LimitedValueValidator implements ConstraintValidator<LimitedValue, Object> {

	private String[] strValues;
	private int[] intValues;
	private boolean allowNullValue;

	@Override
	public void initialize(LimitedValue constraintAnnotation) {
		strValues = constraintAnnotation.strValues();
		intValues = constraintAnnotation.intValues();
		allowNullValue = constraintAnnotation.allowNullValue();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null && allowNullValue) {
			return true;
		}

		if (value instanceof String) {
			for (String s : strValues) {
				if (s.equals(value)) {
					return true;
				}
			}
		} else if (value instanceof Integer) {
			for (Integer s : intValues) {
				if (s == value) {
					return true;
				}
			}
		}

		return false;
	}
}
