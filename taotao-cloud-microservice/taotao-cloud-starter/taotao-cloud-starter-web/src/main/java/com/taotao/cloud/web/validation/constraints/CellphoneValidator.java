package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.lang.StringUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.lang.Validator;

/**
 * 验证是否为手机号码（中国）
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:14:16
 */
public class CellphoneValidator implements ConstraintValidator<Cellphone, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Cellphone constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isMobile(value);
		}

		return !notNull;
	}
	
}
