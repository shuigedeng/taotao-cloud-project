package com.taotao.cloud.java.javase.day12.chapter12_1;
/**
 * 学生类
 * @author wgy
 *
 */
public class Student {
	private String name;
	private int age;
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
	
	@Override
	public boolean equals(Object obj) {
		//1判断是不是同一个对象
		if(this==obj) {
			return true;
		}
		//2判断是否为空
		if(obj==null) {
			return false;
		}
		//3判断是否是Student类型
		if(obj instanceof Student) {
			Student s=(Student)obj;
			//4比较属性
			if(this.name.equals(s.getName())&&this.age==s.getAge()) {
				return true;
			}
		}
		//5不满足条件返回false
		return false;
	}
	
}
