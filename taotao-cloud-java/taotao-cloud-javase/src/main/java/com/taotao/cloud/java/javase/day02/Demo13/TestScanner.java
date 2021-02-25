package com.taotao.cloud.java.javase.day02.Demo13;//package 必须在源文件的首行

import java.util.Scanner;

public class TestScanner {

	public static void main(String[] args) {

		//2.声明Scanner类型的变量
		Scanner input = new Scanner(System.in);

		System.out.println("请输入一个整数：");

		//3.使用
		int i = input.nextInt(); //控制台获取一个整数

		System.out.println("您输入的值为：" + i);
	}
}
