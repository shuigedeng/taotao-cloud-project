package com.taotao.cloud.java.javase.day15.chap15_3;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * 使用OutputStreamWriter写入文件，使用指定的编码
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) throws Exception{
		//1创建OutputStreamWriter
		FileOutputStream fos=new FileOutputStream("d:\\info.txt");
		OutputStreamWriter osw=new OutputStreamWriter(fos, "utf-8");
		//2写入
		for(int i=0;i<10;i++) {
			osw.write("我爱北京，我爱故乡\r\n");
			osw.flush();
		}
		//3关闭
		osw.close();
		System.out.println("执行成功");
	}
}
