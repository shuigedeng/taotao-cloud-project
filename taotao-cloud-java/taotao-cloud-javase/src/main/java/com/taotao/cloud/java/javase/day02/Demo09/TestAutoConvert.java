package com.taotao.cloud.java.javase.day02.Demo09;

public class TestAutoConvert{

	public static void main(String[] args){

		//整数 - 整数

		short s = 123;

		int i = s;//将源类型值存入到目标类型变量中（自动类型转换）

		System.out.println(i);


		byte b = 100;

		short s2 = b;//自动类型转换

		System.out.println(s2);


		//小数 - 小数

		float f = 100.0F;

		double d = f;//自动类型转换

		System.out.println(d);


		//小数 - 整数

		int i2 = 100;

		double d2 = i2;//自动类型转换

		System.out.println(d2);


		//字符 - 整数

		char c = 'A';

		int i3 = c;//自动类型转换

		System.out.println(i3);


		//字符 - 小数

		char c2 = 'a';

		double d3 = c2;

		System.out.println(d3);


		//boolean无法与其他类型进行转换

		boolean bool = true;//true | flase

		int i4 = 0;//不兼容的类型

	}
}







