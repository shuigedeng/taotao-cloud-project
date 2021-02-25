package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.FileInputStream;

/**
 * 使用FileInputStream读取文件
 * 
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception {
		//1创建FileInputStream对象
		FileInputStream fis=new FileInputStream("d:\\hello.txt");
		//2读取
		int data=0;
		while((data=fis.read())!=-1) {//12字节 4个汉字
			System.out.print((char)data);
		}
		//3关闭
		fis.close();
	}
}
