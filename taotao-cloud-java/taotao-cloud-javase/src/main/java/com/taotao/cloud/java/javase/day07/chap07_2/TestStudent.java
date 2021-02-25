package com.taotao.cloud.java.javase.day07.chap07_2;

public class TestStudent {
	public static void main(String[] args) {
		Student xiaoli=new Student(); //(1)内存中开辟一个空间，并赋值属性为默认值 (2)调用构造方法初始化 (3)把对象地址赋值给xiaoli
		xiaoli.name="小明";
		xiaoli.age=20;
		xiaoli.sex="male";
		xiaoli.score=98;
		
		xiaoli.sayHi();
		System.out.println("-----------------");
		Student xiaobai=new Student("小白", 18);
		xiaobai.sayHi();
		
		System.out.println("-----------------");
		Student xiaoliu=new Student("小刘", 19, "female", 99);
		xiaoliu.sayHi();
		
		
	}
}
