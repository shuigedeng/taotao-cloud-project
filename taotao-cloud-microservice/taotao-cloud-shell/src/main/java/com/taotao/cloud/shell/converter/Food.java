package com.taotao.cloud.shell.converter;

// 自定义类型
public class Food {
	private String value = null;

	public Food(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("Food{").append("value='").append(value).append("'}")
			.toString();
	}
}
