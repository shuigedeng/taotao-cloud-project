package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.function.BiFunction;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 使用字节缓冲流读取
 * BufferedInputStream
 * @author shuigedeng
 *
 */
public class Demo4 {
	public static void main(String[] args) throws Exception{
		//1创建BufferedInputStream
		FileInputStream fis=new FileInputStream("d:\\aaa.txt");
		BufferedInputStream bis=new BufferedInputStream(fis);
		//2读取
//		int data=0;
//		while((data=bis.read())!=-1) {
//			System.out.print((char)data);
//		}
		//
		byte[] buf=new byte[1024];
		int count=0;
		while((count=bis.read(buf))!=-1) {
			System.out.println(new String(buf,0,count));
		}
		
		//3关闭
		bis.close();
		
	}
}
