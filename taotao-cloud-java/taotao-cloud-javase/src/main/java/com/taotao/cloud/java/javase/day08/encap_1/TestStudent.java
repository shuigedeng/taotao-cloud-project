package com.taotao.cloud.java.javase.day08.encap_1;

public class TestStudent {
	public static void main(String[] args) {
		Student s1=new Student();
		//s1.name="张三";
		s1.setName("张三");
		//s1.age=1000;
		s1.setAge(1000);
		//s1.sex="妖";
		s1.setSex("妖");
		//s1.score=100;
		s1.setScore(100);
		s1.sayHi();
	}
}
