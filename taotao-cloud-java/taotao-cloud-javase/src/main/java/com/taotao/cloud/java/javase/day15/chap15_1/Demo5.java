package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * 使用字节缓冲流写入文件
 * BufferedOutputStream
 * 
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) throws Exception{
		//1创建字节输出缓冲流
		FileOutputStream fos=new FileOutputStream("d:\\buffer.txt");
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		//2写入文件
		for(int i=0;i<10;i++) {
			bos.write("helloworld\r\n".getBytes());//写入8K缓冲区 
			bos.flush();//刷新到硬盘
		}
		//3关闭(内部调用flush方法)
		bos.close();
		
		
	}
}
