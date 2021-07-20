package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.Serializable;



/**
 * 学生类
 * @author shuigedeng
 *
 */
public class Student implements Serializable{

	/**
	 * serialVersionUID:序列化版本号ID,
	 */
	private static final long serialVersionUID = 100L;
	private String name;
	private transient int age;
	
	public static String country="中国";
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	public Student(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + "]";
	}
	

}
