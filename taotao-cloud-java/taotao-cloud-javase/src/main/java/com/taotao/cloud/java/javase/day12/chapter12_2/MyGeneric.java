package com.taotao.cloud.java.javase.day12.chapter12_2;
/**
 * 泛型类
 * 语法：类名<T>
 * T是类型占位符,表示一种引用类型,如果编写多个使用逗号隔开
 * @author shuigedeng
 *
 */
public class MyGeneric<T> {
	//使用泛型T
	//1创建变量
	T t;
	
	//2泛型作为方法的参数
	public void show(T t) {
		System.out.println(t);
	}
	//3泛型作为方法的返回值
	public T getT() {
		return t;
	}

}
