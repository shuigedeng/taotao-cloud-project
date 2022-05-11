package com.taotao.cloud.web.validation.constraints;

import cn.hutool.core.lang.Validator;
import com.taotao.cloud.common.utils.lang.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证是否为IPV4地址
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:15:15
 */
public class IPV4Validator implements ConstraintValidator<IPV4, String> {

	private boolean notNull;

	@Override
	public void initialize(IPV4 constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtil.isNotBlank(value)) {
			return Validator.isIpv4(value);
		}

		return !notNull;
	}

}
