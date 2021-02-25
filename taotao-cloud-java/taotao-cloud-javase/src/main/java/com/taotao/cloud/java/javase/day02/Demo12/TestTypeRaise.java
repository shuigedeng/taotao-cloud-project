package com.taotao.cloud.java.javase.day02.Demo12;

public class TestTypeRaise{

	public static void main(String[] args){

		double d1 = 10.0;

		int i1 = 5;

		double d2 = d1 + i1;

		System.out.println(d2);

		//-----------------------------------------

		float f1 = 5.0F;

		short s1 = 20;

		float f2 = f1 + s1;

		System.out.println(f2);

		//-----------------------------------------

		long l1 = 100;

		byte b1 = 50;

		long l2 = l1 + b1;

		System.out.println(l2);

		//----------------------------------------

		int i3 = 123;

		short s3 = 456;

		int i4 = i3 + s3;

		System.out.println(i4);

		//--------------------------------------

		short s4 = 321;

		byte b3 = 111;

		int s5 = s4 + b3;

		System.out.println(s5);


		//--------------------------------------

		//特殊：String的字符串拼接

		String str = "Hello";

		int i5 = 123;

		String str2 = str + i5;

		System.out.println(str2);


		int javaScore = 100;

		String str3 = "Java的成绩是：" + javaScore;

		System.out.println(str3);

		System.out.println( "Java的成绩是：" + javaScore );

	}
}
