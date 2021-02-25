package com.taotao.cloud.java.javase.day07.chap07_1;
/**
 * 老师类
 * 属性
 * 	name
 * 	age
 *  salary
 * 方法
 *  sayHi()
 *  know(Student s); 
 * @author wgy
 *
 */
public class Teacher {
	String name;//姓名
	int age;//年龄
	double salary;//工资
	
	//介绍
	public void sayHi() {
		System.out.println("同学们好,我是"+name+" 年龄:"+age);
	}
	
	public void know(Student s) {
		System.out.println(name+"要认识学生");
		s.sayHi();
	}
	
}
