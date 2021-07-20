package com.taotao.cloud.java.javase.day17.chap17_1;

import java.io.Serializable;

/**
 * Person类
 * @author shuigedeng
 *
 */
public class Person implements Serializable,Cloneable{
	//姓名
	private String name;
	//年龄
	private int age;
	
	public Person() {
		System.out.println("无参构造执行了...");
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
		System.out.println("带参构造方法执行了...");
	}


	//吃
	public void eat() {
		System.out.println(name+"正在吃东西......");
	}


	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
	//带参的方法
	public void eat(String food) {
		System.out.println(name+"开始吃...."+food);
	}
	
	//私有的方法
	private void privateMethod() {
		System.out.println("这是一个私有方法");
	}
	
	//静态方法
	public static void staticMethod() {
		System.out.println("这是一个静态方法");
	}
}
