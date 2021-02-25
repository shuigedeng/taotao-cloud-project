package com.taotao.cloud.java.javase.day02.Demo10;

public class TestForceConvert{

	public static void main(String[] args){

		//长度足够，数据完整
		short s = 123;

		byte b = (byte)s;//强制类型转换（数据完整）

		System.out.println(b);

		//长度不够，数据截断

		short s2 = 257;

		byte b2 = (byte)s2;//强制类型转换（数据截断）

		System.out.println(b2);


		short s3 = 130;

		byte b3 = (byte)s3;

		System.out.println(b3);


		//小数 强转 整数

		double d = 2.999;

		int i = (int)d;

		System.out.println(i);


		//字符 强转 整数

		char c = 'A';

		int i2 = c;//自动类型转换

		System.out.println(i2);


		char c2 = (char)i2;//强制类型转换

		System.out.println(c2);


		//字符与整数转换的注意事项

		short s4 = -1;// -32768 ~ 32767

		char c3 = (char)s4;//强制类型转换

		System.out.println(c3);
	}
}
