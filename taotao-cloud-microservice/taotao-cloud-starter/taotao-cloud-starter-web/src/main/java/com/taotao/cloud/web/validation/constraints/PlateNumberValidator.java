package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.lang.StringUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.lang.Validator;

/**
 * 校验车牌号
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:15:42
 */
public class PlateNumberValidator implements ConstraintValidator<PlateNumber, String> {

	private boolean notNull;
	
	@Override
	public void initialize(PlateNumber constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isPlateNumber(value);
		}

		return !notNull;
	}
	
}
