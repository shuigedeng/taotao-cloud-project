package com.taotao.cloud.web.mvc.validator;

import com.taotao.cloud.web.mvc.constraints.JoinLength;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * 限定值校验器
 * 
 * @createTime 2019年10月23日 上午10:18:20
 *
 */
public class JoinLengthValidator implements ConstraintValidator<JoinLength, String>{

	private String symbol;
	private int limitSize;
 
	@Override
	public void initialize(JoinLength constraintAnnotation) {
		symbol = constraintAnnotation.symbol();
		limitSize = constraintAnnotation.limitSize();
	}
 
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.length() == 0){
			return true;
		}
		return value.split(symbol).length <= this.limitSize;
	}
}
