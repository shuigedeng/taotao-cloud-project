package com.taotao.cloud.java.javase.day13.chapter13_1;

import java.util.Scanner;

/**
 * throws:声明异常
 * @author shuigedeng
 *
 */
public class Demo7 {
	public static void main(String[] args){//JVM
		try {
			divide();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	public static void divide() throws Exception {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入第一个数字");
		int num1=input.nextInt();
		System.out.println("请输入第二个数字");
		int num2=input.nextInt();
		int result=num1/num2;
		System.out.println("结果:"+result);
	}
}
