package com.taotao.cloud.java.javase.day12.chapter12_2;

public class MyInterfaceImpl2<T> implements MyInterface<T>{

	@Override
	public T server(T t) {
		System.out.println(t);
		return t;
	}

}
