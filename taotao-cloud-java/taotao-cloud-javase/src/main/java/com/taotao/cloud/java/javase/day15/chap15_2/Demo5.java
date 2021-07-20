package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.BufferedReader;
import java.io.FileReader;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 使用字符缓冲流读取文件
 * BufferedReader
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) throws Exception{
		//1创建缓冲流
		FileReader fr=new FileReader("d:\\write.txt");
		BufferedReader br=new BufferedReader(fr);
		//2读取
		//2.1第一种方式
//		char[] buf=new char[1024];
//		int count=0;
//		while((count=br.read(buf))!=-1) {
//			System.out.print(new String(buf,0,count));
//		}
		//2.2第二种方式，一行一行的读取
		String line=null;
		while((line=br.readLine())!=null) {
			System.out.println(line);
		}
		
		//3关闭
		br.close();
	}
}
