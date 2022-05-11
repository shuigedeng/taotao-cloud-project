package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;

import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 邮政编码（中国）校验器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:16:02
 */
public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> {

	private boolean notNull;
	
	@Override
	public void initialize(ZipCode constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isZipCode(value);
		}

		return !notNull;
	}
	
}
