package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 演示多重catch的使用
 * try...catch(类型1)...catch(类型2)...
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		int result=0;
		try {
//			String string=null;
//			System.out.println(string.equals("hello"));
			System.out.println("请输入第一个数字");
			int num1=input.nextInt();//InputMismatchException
			System.out.println("请输入第二个数字");
			int num2=input.nextInt();
			result=num1/num2;//发生异常// ArethmicException
		}catch (ArithmeticException e) {//捕获 Exception：是所有异常的父类	
			System.out.println("算术异常");
		}catch (InputMismatchException e) {
			System.out.println("输入不匹配异常");
		}catch (Exception e) {
			System.out.println("未知异常");
		}
		System.out.println("结果是:"+result);
		System.out.println("程序结束了...");
	}
}
