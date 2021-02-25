package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.Scanner;

/**
 * 演示try...catch...finally...使用
 * finally最后的
 * 表示有没有发生异常都会执行的代码
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		int result=0;
		try {
			System.out.println("请输入第一个数字");
			int num1=input.nextInt();//InputMismatchException
			System.out.println("请输入第二个数字");
			int num2=input.nextInt();
			result=num1/num2;//发生异常// ArethmicException
			//手动退出JVM
			//System.exit(0);
		}catch (Exception e) {//捕获 Exception：是所有异常的父类
			//处理
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}finally {
			System.out.println("释放资源...");
		}
		System.out.println("结果是:"+result);
		System.out.println("程序结束了...");
	}
}
