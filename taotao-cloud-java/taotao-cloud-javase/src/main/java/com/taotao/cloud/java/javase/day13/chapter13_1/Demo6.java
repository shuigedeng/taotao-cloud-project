package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * try...finally的使用
 * 不能处理异常，可以释放资源，把异常向上抛出
 * @author shuigedeng
 *
 */
public class Demo6 {
	public static void main(String[] args) {//JVM
		try {
			divide();
		}catch (Exception e) {
			System.out.println("出现异常:"+e.getMessage());
		}
	}
	public static void divide() {
		Scanner input=new Scanner(System.in);
		int result=0;
		try {
			System.out.println("请输入第一个数字");
			int num1=input.nextInt();//InputMismatchException
			System.out.println("请输入第二个数字");
			int num2=input.nextInt();
			result=num1/num2;//发生异常// ArethmicException
		}finally {
			System.out.println("释放资源");
		}
		System.out.println("结果是:"+result);
		System.out.println("程序结束了...");
	}
}
