package com.taotao.cloud.web.validation.constraints;

import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.lang.Validator;

/**
 * 验证是否为IPV6地址
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:15:25
 */
public class IPV6Validator implements ConstraintValidator<IPV6, String> {

	private boolean notNull;
	
	@Override
	public void initialize(IPV6 constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isIpv6(value);
		}

		return !notNull;
	}
	
}
