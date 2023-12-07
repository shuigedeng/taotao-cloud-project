package com.taotao.cloud.shell.converter;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

// 使用自定义转换类型
@ShellComponent
public class ConvertionCmd {
	//#food apple
	// Food{value='apple'}
	// 在命令方法中直接可以获取Food对象，这是通过前面的自定义类型转换器MyConverter实现的
	@ShellMethod("Conversion food")
	public String food(Food food) {
		return food.toString();
	}
}
