package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.PrintWriter;

/**
 * 演示PrintWriter的使用
 * 
 */
public class Demo7 {
	public static void main(String[] args) throws Exception {
		//1创建打印流
		PrintWriter pw=new PrintWriter("d:\\print.txt");
		//2打印
		pw.println(97);
		pw.println(true);
		pw.println(3.14);
		pw.println('a');
		//3关闭
		pw.close();
		System.out.println("执行完毕");
	}
}
