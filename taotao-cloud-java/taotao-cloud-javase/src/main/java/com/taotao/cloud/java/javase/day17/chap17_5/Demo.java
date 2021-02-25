package com.taotao.cloud.java.javase.day17.chap17_5;

import java.lang.reflect.Method;

public class Demo {
	public static void main(String[] args) throws Exception{
		//(1)获取类对象
		Class<?> class1=Class.forName("com.qf.chap17_5.Person");
		//(2)获取方法
		Method method=class1.getMethod("show", String.class,int.class,String.class);
		//(3)获取方法上面的注解信息 personInfo=null
		PersonInfo personInfo=method.getAnnotation(PersonInfo.class);
		
		//(4)打印注解信息
		System.out.println(personInfo.name());
		System.out.println(personInfo.age());
		System.out.println(personInfo.sex());
		
		//(5)调用方法
		Person yueyue=(Person)class1.newInstance();
		method.invoke(yueyue, personInfo.name(),personInfo.age(),personInfo.sex());
		
		
	}
}
