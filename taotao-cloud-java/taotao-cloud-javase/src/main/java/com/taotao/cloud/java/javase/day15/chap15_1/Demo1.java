package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.FileInputStream;

/**
 * 演示FileInputStream的使用
 * 文件字节输入流
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception{
		//1创建FileInputStream,并指定文件路径
		FileInputStream fis=new FileInputStream("d:\\aaa.txt");
		//2读取文件
		//fis.read()
		//2.1单个字节读取
//		int data=0;
//		while((data=fis.read())!=-1) {
//			System.out.print((char)data);
//		}
		//2.2一次读取多个字节
		
		byte[] buf=new byte[1024];
		int count=0;
		while((count=fis.read(buf))!=-1) {
			System.out.println(new String(buf,0,count));
		}
		
		//3关闭
		fis.close();
		System.out.println();
		System.out.println("执行完毕");
	}
}
