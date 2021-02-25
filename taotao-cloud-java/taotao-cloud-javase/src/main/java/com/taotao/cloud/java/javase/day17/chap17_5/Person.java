package com.taotao.cloud.java.javase.day17.chap17_5;

public class Person {

	@MyAnnotation()
	public void show() {
		
	}
	
	//@MyAnnotation2(value="大肉",num=25)
	public void eat() {
		
	}
	
	@PersonInfo(name="小岳岳",age=30,sex="男")
	public void show(String name,int age,String sex) {
		System.out.println(name+"==="+age+"===="+sex);
	}
	
	
}
