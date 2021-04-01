package com.taotao.cloud.web.mvc.validator;

import com.taotao.cloud.web.mvc.constraints.LimitedValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * 限定值校验器
 * 
 * @createTime 2019年10月23日 上午10:18:20
 *
 */
public class LimitedValueValidator implements ConstraintValidator<LimitedValue, Object>{

	private String[] strValues;
	private int[] intValues;
	private boolean allowNullValue;
 
	@Override
	public void initialize(LimitedValue constraintAnnotation) {
		strValues = constraintAnnotation.strValues();
		intValues = constraintAnnotation.intValues();
		allowNullValue = constraintAnnotation.allowNullValue();
	}
 
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context ) {
		if(value == null && allowNullValue){
			return true;
		}
		if(value instanceof String) {
			for (String s:strValues) {
				if(s.equals(value)){
					return true;
				}
			}
		}else if(value instanceof Integer){
			for (Integer s:intValues) {
				if(s==value){
					return true;
				}
			}
		}
		return false;
	}
}
