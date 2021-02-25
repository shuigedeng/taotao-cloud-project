package com.taotao.cloud.java.javase.day02.Demo03;

/**
 基本数据类型（整数）
 */
public class TestType{
	public static void main(String[] args){

		//数据类型 变量名 = 值;

		byte b = 127;// -128 ~ 127 （共计256个整数）

		System.out.println(b);


		short s = 32767;//-32768 ~ 32767 （共计65536个整数）

		System.out.println(s);


		int i = 2147483647;//-2147483648 ~ 2147483647 （共计42亿多个整数）

		System.out.println(i);



		//Java中所有的“整数字面值”的默认类型是int，当整数字面值超过int的取值范围时，则提醒“过大的整数”

		long l1 = 2147483648L;//显示告知JVM，此值为long类型
		long l2 = 9223372036854775807L;//-9223372036854775808L ~ 9223372036854775807L

		System.out.println(l1);
		System.out.println(l2);
	}
}
