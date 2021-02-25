package com.taotao.cloud.java.javase.day11.chapter11;

public class Student  {
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
		//1判断两个对象是否是同一个引用
		if(this==obj) {
			return true;
		}
		//2判断obj是否null
		if(obj==null) {
			return false;
		}
		//3判断是否是同一个类型
//		if(this.getClass()==obj.getClass()) {
//			
//		}
		//intanceof 判断对象是否是某种类型
		if(obj instanceof Student) {
			//4强制类型转换
			Student s=(Student)obj;
			//5比较熟悉
			if(this.name.equals(s.getName())&&this.age==s.getAge()) {
				return true;
			}
			
			
		}
		
		
		return false;
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println(this.name+"对象被回收了");
	}
	
	
	
}
