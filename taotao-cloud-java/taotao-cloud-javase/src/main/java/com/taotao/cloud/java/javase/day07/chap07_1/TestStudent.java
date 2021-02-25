package com.taotao.cloud.java.javase.day07.chap07_1;

public class TestStudent {
	public static void main(String[] args) {
		//创建对象
		Student xiaoming=new Student();
		//属性赋值
		xiaoming.name="小明";
		xiaoming.age=18;
		xiaoming.sex="男";
		xiaoming.score=99;
		//调用方法
		xiaoming.sayHi();
		
		//创建对象
		Student xiaoli=new Student();
		//属性赋值
		xiaoli.name="晓丽";
		xiaoli.age=20;
		xiaoli.sex="女";
		xiaoli.score=100;
		//调用方法
		xiaoli.sayHi();
		
	}
}
