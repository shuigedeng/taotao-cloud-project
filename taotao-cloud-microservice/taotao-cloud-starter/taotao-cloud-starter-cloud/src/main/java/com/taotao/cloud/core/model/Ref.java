package com.taotao.cloud.core.model;


/**
 * @author: chejiangyi
 * @version: 2019-08-02 11:18 模拟out和ref语法
 **/
public class Ref<T> {

	private volatile T data;

	public Ref(T data) {
		this.data = data;
	}

	public boolean isNull() {
		return data == null;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
