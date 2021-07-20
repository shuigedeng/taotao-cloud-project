package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.Scanner;

/**
 * 演示异常的产生和传递
 * 要求：输入两个数字实现两个数字相除
 * @author shuigedeng
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		operation();
	}
	public static void operation() {
		System.out.println("---opration-----");
		divide();
	}
	public static void divide() {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入第一个数字");
		int num1=input.nextInt();//出现异常，没有处理，程序中断
		System.out.println("请输入第二个数字");
		int num2=input.nextInt();
		int result=num1/num2;//出现异常没有处理，所以程序中断
		System.out.println("结果:"+result);
		System.out.println("程序执行完毕了...");
	}
}
