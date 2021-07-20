package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.FileReader;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 使用FileReader读取文件
 * @author shuigedeng
 *
 */
public class Demo2 {
	public static void main(String[] args) throws Exception{
		//1创建FileReader 文件字符输入流
		FileReader fr=new FileReader("d:\\hello.txt");
		//2读取
		//2.1单个字符读取
//		int data=0;
//		while((data=fr.read())!=-1) {//读取一个字符
//			System.out.print((char)data);
//		}
		char[] buf=new char[1024];
		int count=0;
		while((count=fr.read(buf))!=-1) {
			System.out.println(new String(buf, 0, count));
		}
		
		//3关闭
		fr.close();
	}
}
