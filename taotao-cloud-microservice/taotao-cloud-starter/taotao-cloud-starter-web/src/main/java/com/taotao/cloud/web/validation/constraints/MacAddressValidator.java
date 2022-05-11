package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.lang.Validator;

/**
 * 验证是否为MAC地址
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:15:34
 */
public class MacAddressValidator implements ConstraintValidator<MacAddress, String> {

	private boolean notNull;
	
	@Override
	public void initialize(MacAddress constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isMac(value);
		}

		return !notNull;
	}
	
}
