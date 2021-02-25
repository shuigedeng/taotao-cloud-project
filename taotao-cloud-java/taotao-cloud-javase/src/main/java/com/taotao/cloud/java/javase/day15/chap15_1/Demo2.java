package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.FileOutputStream;

/**
 * 演示文件字节输出流的使用
 * FileOutputStream
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) throws Exception{
		//1创建文件字节输出流对象
		FileOutputStream fos=new FileOutputStream("d:\\bbb.txt",true);
		//2写入文件
//		fos.write(97);
//		fos.write('b');
//		fos.write('c');
		String string="helloworld";
		fos.write(string.getBytes());
		//3关闭
		fos.close();
		System.out.println("执行完毕");
	}
}
