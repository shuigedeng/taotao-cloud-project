package com.taotao.cloud.java.javase.day08.inheritance_4;

public class A {
	int num1;
	int num2;
	public A() {
		System.out.println("A的默认构造方法");
	}
	//带参构造方法
	public A(int num1,int num2) {
		System.out.println("A的带参构造");
		this.num1=num1;
		this.num2=num2;
	}
	
	public void m1() {
		System.out.println("A类的m1方法");
	}
}
