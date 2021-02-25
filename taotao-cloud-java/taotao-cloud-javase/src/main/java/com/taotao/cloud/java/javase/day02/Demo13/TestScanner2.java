package com.taotao.cloud.java.javase.day02.Demo13;

import java.util.Scanner;

public class TestScanner2{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		System.out.println("请输入值：");

		int i = input.nextInt();//接收整数

		double d = input.nextDouble();//接收小数

		String s = input.next();//接收字符串

		char c = input.next().charAt(0);//接收字符（接收一个完整的字符串，获取其中的第一个字符）


		System.out.println("整数：" + i);
		System.out.println("小数：" + d);
		System.out.println("字符串：" + s);
		System.out.println("字符：" + c);


	}
}
