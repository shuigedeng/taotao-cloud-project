package com.taotao.cloud.java.javase.day11.chapter11;

public class TestStudent2 {
	public static void main(String[] args) {
//		Student s1=new Student("aaa", 20);
//		Student s2=new Student("bbb", 20);
//		Student s3=new Student("ccc", 20);
//		Student s4=new Student("ddd", 20);
//		Student s5=new Student("eee", 20);
		new Student("aaa", 20);
		new Student("bbb", 20);
		new Student("ccc", 20);
		new Student("ddd", 20);
		new Student("eee", 20);
		//回收垃圾
		System.gc();
		System.out.println("回收垃圾");
		
	}
}
