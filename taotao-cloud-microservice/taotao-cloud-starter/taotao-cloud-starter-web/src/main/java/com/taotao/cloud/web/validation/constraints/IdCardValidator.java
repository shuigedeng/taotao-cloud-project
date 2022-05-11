package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;
import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证是否为身份证号码（支持18位、15位和港澳台的10位
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:15:04
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

	private boolean notNull;

	@Override
	public void initialize(IdCard constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isCitizenId(value);
		}

		return !notNull;
	}

}
