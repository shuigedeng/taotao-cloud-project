package com.taotao.cloud.java.javase.day09.static_1;

public class TestTeacher {
	public static void main(String[] args) {
		System.out.println("对象创建之前次数:"+Teacher.count);
		Teacher t1=new Teacher();
		Teacher t2=new Teacher();
		Teacher t3=new Teacher();
		System.out.println("对象创建之后次数:"+Teacher.count);
	}
}
