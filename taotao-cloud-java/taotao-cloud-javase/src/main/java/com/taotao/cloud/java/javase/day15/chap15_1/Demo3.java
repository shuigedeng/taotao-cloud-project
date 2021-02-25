package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 使用文件字节流实现文件的复制
 * @author wgy
 *
 */
public class Demo3 {
	public static void main(String[] args) throws Exception{
		//1创建流
		//1.1文件字节输入流
		FileInputStream fis=new FileInputStream("d:\\001.jpg");
		//1.2文件字节输出流
		FileOutputStream fos=new FileOutputStream("d:\\002.jpg");
		//2一边读，一边写
		byte[] buf=new byte[1024];
		int count=0;
		while((count=fis.read(buf))!=-1) {
			fos.write(buf,0,count);
		}
		//3关闭
		fis.close();
		fos.close();
		System.out.println("复制完毕");
		
		
	}
}
