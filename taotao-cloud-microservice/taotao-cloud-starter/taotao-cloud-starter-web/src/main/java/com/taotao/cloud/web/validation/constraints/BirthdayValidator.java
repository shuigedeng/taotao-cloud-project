package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.lang.StringUtil;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;

/**
 * 生日格式校验器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:13:25
 */
public class BirthdayValidator implements ConstraintValidator<Birthday, Object> {

	private boolean notNull;
	
	@Override
	public void initialize(Birthday constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String validValue = null;
		if (value instanceof String) {
			validValue = (String) value;
		} else if (value instanceof Date) {
			validValue = DateUtil.formatDate((Date) value);
		} else if (value instanceof TemporalAccessor) {
			validValue = com.taotao.cloud.common.utils.date.DateUtil.toDateFormatter((TemporalAccessor) value);
		}
		
		if (StringUtil.isNotBlank(validValue)) {
			return Validator.isBirthday(validValue);
		}

		return !notNull;
	}
	
}
