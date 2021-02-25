package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.FileWriter;

/**
 * 使用FileWriter写入文件
 * @author wgy
 *
 */
public class Demo3 {
	public static void main(String[] args) throws Exception {
		//1创建FileWriter对象
		FileWriter fw=new FileWriter("d:\\write.txt");
		//2写入
		for(int i=0;i<10;i++) {
			fw.write("java是世界上最好的语言\r\n");
			fw.flush();
		}
		//3关闭
		fw.close();
		System.out.println("执行完毕");
	}
}
