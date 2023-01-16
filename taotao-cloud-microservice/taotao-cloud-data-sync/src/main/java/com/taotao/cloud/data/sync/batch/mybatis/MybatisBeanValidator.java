package com.taotao.cloud.data.sync.batch.mybatis;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/2017
 * @Description :
 **/
public class MybatisBeanValidator<T> implements Validator<T>, InitializingBean {

	private jakarta.validation.Validator validator;

	@Override
	public void validate(T value) throws ValidationException {
		/**
		 * 使用Validator的validate方法校验数据
		 */
		Set<ConstraintViolation<T>> constraintViolations =
			validator.validate(value);
		if (constraintViolations.size() > 0) {
			StringBuilder message = new StringBuilder();
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {
				message.append(constraintViolation.getMessage() + "\n");
			}
			throw new ValidationException(message.toString());
		}
	}

	/**
	 * 使用JSR-303的Validator来校验我们的数据，在此进行JSR-303的Validator的初始化
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		ValidatorFactory validatorFactory =
			Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.usingContext().getValidator();
	}

}
