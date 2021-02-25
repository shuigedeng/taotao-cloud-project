package com.taotao.cloud.java.javase.day13.chapter13_1;

public class TestPerson {
	public static void main(String[] args) throws Exception{
		Person xiaozhang=new Person();
		xiaozhang.setAge(20);
		xiaozhang.setSex("妖");
		System.out.println(xiaozhang.toString());
	}
}
