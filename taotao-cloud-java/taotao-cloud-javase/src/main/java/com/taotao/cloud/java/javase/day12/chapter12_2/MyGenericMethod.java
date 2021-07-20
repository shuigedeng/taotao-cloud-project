package com.taotao.cloud.java.javase.day12.chapter12_2;
/**
 * 泛型方法
 * 语法：<T> 返回值类型
 * @author shuigedeng
 *
 */
public class MyGenericMethod {
	
	//泛型方法
	public  <T> T show(T t) {
		System.out.println("泛型方法"+t);
		return t;
	}
	
	
}
