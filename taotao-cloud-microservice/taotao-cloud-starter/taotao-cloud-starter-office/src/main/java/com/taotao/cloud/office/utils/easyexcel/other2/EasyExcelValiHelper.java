package com.taotao.cloud.office.utils.easyexcel.other2;

import com.alibaba.excel.annotation.ExcelProperty;
import java.lang.reflect.Field;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;


public class EasyExcelValiHelper {

	private EasyExcelValiHelper() {
	}

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory()
		.getValidator();

	public static <T> String validateEntity(T obj) throws NoSuchFieldException {
		StringBuilder result = new StringBuilder();
		Set<ConstraintViolation<T>> set = VALIDATOR.validate(obj, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<T> cv : set) {
				Field declaredField = obj.getClass()
					.getDeclaredField(cv.getPropertyPath().toString());
				ExcelProperty annotation = declaredField.getAnnotation(ExcelProperty.class);
				//拼接错误信息，包含当前出错数据的标题名字+错误信息
				result.append(annotation.value()[0]).append(cv.getMessage()).append(";");
			}
		}
		return result.toString();
	}
}
