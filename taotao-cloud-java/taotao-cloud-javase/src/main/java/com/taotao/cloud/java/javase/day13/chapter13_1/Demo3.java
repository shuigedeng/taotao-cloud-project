package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.Scanner;

/**
 * 演示try...catch...语句的使用
 * try{...可能发生异常的代码}
 * catch{...捕获异常，并处理异常}
 * 三种情况：
 * （1）正常没有发生异常
 * （2）发生异常并捕获
 * （3）发生异常，不能捕获
 * @author wgy
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		int result=0;
		try {
			System.out.println("请输入第一个数字");
			int num1=input.nextInt();//InputMismatchException
			System.out.println("请输入第二个数字");
			int num2=input.nextInt();
			result=num1/num2;//发生异常// ArethmicException
		}catch (Exception e) {//捕获 Exception：是所有异常的父类
			//处理
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("结果是:"+result);
		System.out.println("程序结束了...");
				
	}
}
