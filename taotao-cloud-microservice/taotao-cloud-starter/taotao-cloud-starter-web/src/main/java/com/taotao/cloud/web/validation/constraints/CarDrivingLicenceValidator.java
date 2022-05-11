package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;

import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 驾驶证格式校验器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:13:50
 */
public class CarDrivingLicenceValidator implements ConstraintValidator<CarDrivingLicence, String> {

	private boolean notNull;
	
	@Override
	public void initialize(CarDrivingLicence constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isCarDrivingLicence(value);
		}

		return !notNull;
	}
	
}
