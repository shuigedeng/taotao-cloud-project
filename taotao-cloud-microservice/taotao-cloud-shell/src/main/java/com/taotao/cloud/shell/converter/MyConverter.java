package com.taotao.cloud.shell.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// 自定义类型转换器
@Component
public class MyConverter implements Converter<String, Food> {
	@Override
	public Food convert(String s) {
		// 将输入参数转换为Food类型实例
		return new Food(s);
	}
}
