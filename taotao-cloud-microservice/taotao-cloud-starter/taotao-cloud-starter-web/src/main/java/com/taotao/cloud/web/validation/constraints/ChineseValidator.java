package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.lang.StringUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证是否都为汉字
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:14:29
 */
public class ChineseValidator implements ConstraintValidator<Chinese, Object> {

	private boolean notNull;

	@Override
	public void initialize(Chinese constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String validValue = null;
		if ((value != null && CharUtil.isChar(value) && !CharUtil.isBlankChar((char) value))
			|| (value instanceof String && StrUtil.isNotBlank((String) value))) {
			validValue = StrUtil.toString(value);
		}

		if (StringUtils.isNotBlank(validValue)) {
			return Validator.isChinese(validValue);
		}

		return !notNull;
	}

}
