package com.taotao.cloud.java.javase.day08.inheritance_3;

public class Son extends Father{
	
	int value=200;
	@Override
	public void show() {
		super.show();//调用父类的show方法
		System.out.println("Son的Show");
	}
	
	public void print() {
		int value=300;
		System.out.println(value);//局部遍历
		System.out.println(this.value);//本类的实例变量
		System.out.println(super.value);//父类的实例变量
	}
}
