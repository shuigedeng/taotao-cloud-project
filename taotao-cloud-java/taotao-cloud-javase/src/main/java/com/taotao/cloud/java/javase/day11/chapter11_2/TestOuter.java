package com.taotao.cloud.java.javase.day11.chapter11_2;

public class TestOuter {
	public static void main(String[] args) {
		//直接创建静态内部类对象
		Outer.Inner inner=new Outer.Inner();
		//调用方法
		inner.show();
		
	}
}
