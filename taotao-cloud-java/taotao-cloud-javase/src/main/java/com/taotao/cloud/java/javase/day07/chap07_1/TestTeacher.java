package com.taotao.cloud.java.javase.day07.chap07_1;

public class TestTeacher {
	public static void main(String[] args) {
		//创建老师对象
		Teacher wang=new Teacher();
		wang.name="王老师";
		wang.age=35;
		wang.salary=50000;
		//创建学生对象
		//int num=100;
		Student xiaoming=new Student();
		xiaoming.name="小明";
		xiaoming.age=18;
		xiaoming.sex="男";
		xiaoming.score=95;
		
		//调用方法
		wang.sayHi();
		wang.know(xiaoming);
		
	}
}
