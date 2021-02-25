package com.taotao.cloud.java.javase.day11.chapter11_5;

public class Demo1 {
	public static void main(String[] args) {
//		int num=10;
		//类型转换:装箱,基本类型转成引用类型的过程
		//基本类型
		int num1=18;
		//使用Integer类创建对象
		Integer integer1=new Integer(num1);
		Integer integer2=Integer.valueOf(num1);
		System.out.println("装箱");
		System.out.println(integer1);
		System.out.println(integer2);
		
		//类型转型:拆箱,引用类型转成基本类型
		Integer integer3=new Integer(100);
		int num2=integer3.intValue();
		System.out.println("拆箱");
		System.out.println(num2);
		

		//JDK1.5之后,提供自动装箱和拆箱
		int age=30;
		//自动装箱
		Integer integer4=age;
		System.out.println("自动装箱");
		System.out.println(integer4);
		//自动拆箱
		int age2=integer4;
		System.out.println("自动拆箱");
		System.out.println(age2);
		
		System.out.println("-------------基本类型和字符串之间转换----------------");
		//基本类型和字符串之间转换
		//1 基本类型转成字符串
		int n1=255;
		//1.1使用+号
		String s1=n1+"";
		//1.2使用Integer中的toString()方法
		String s2=Integer.toString(n1, 16);//f
		System.out.println(s1);
		System.out.println(s2);
		
		
		//2字符串转成基本类型
		String str="150";
		//使用Integer.parseXXX();
		int n2=Integer.parseInt(str);
		System.out.println(n2);
		
		//boolean字符串形式转成基本类型，"true"--->true  非"true"---->false
		String str2="false";
		boolean b1=Boolean.parseBoolean(str2);
		System.out.println(b1);
	
		
		
		
		
		
		
	}
}
