package com.taotao.cloud.java.javase.day13.chapter13_1;

public class Person {
	private String name;
	private String sex;
	private int age;
	public Person() {
		// TODO Auto-generated constructor stub
	}
	public Person(String name, String sex, int age) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) throws Exception {
		if(sex.equals("男")||sex.equals("女")) {
			this.sex = sex;
		}else {
			throw new Exception("性别不符合要求");
		}
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) throws Exception {
		if(age>0&&age<=120) {
			this.age = age;
		}else {
			//抛出异常
			throw new Exception("年龄不符合要求");
		}
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", sex=" + sex + ", age=" + age + "]";
	}
	
}
