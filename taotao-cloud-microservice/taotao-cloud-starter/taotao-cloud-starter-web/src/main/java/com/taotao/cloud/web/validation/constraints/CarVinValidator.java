package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;

import com.taotao.cloud.common.utils.lang.StringUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 车架号校验器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:14:05
 */
public class CarVinValidator implements ConstraintValidator<CarVin, String> {

	private boolean notNull;
	
	@Override
	public void initialize(CarVin constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isCarVin(value);
		}

		return !notNull;
	}
	
}
