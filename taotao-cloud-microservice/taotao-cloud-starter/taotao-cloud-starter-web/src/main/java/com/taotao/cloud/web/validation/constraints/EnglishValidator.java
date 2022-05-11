package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;
import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证该字符串是否是字母（包括大写和小写字母）
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:14:51
 */
public class EnglishValidator implements ConstraintValidator<English, String> {

	private boolean notNull;

	@Override
	public void initialize(English constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isWord(value);
		}

		return !notNull;
	}

}
