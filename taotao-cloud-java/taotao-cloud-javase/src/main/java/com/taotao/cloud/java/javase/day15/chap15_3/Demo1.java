package com.taotao.cloud.java.javase.day15.chap15_3;

import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 使用InputStreamReader读取文件，指定使用的编码
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception {
		//1创建InputStreamReader对象
		FileInputStream fis=new FileInputStream("d:\\write.txt");
		InputStreamReader isr=new InputStreamReader(fis, "gbk");
		//2读取文件
		int data=0;
		while((data=isr.read())!=-1) {
			System.out.print((char)data);
		}
		//3关闭
		isr.close();
	}
}
