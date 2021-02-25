package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * 使用FileReader和FileWriter复制文本文件,不能复制图片或二进制文件
 * 使用字节流复制任意文件。
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) throws Exception{
		//1创建FIleReader FileWriter
		FileReader fr=new FileReader("d:\\001.jpg");
		FileWriter fw=new FileWriter("d:\\003.jpg");
		//2读写
		int data=0;
		while((data=fr.read())!=-1) {
			fw.write(data);
			fw.flush();
		}
		//3关闭
		fr.close();
		fw.close();
		System.out.println("复制完毕");
		
	}
}
