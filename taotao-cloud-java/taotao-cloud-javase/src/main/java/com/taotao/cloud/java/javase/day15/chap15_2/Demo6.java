package com.taotao.cloud.java.javase.day15.chap15_2;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * 演示BufferedWriter的使用
 * @author wgy
 *
 */
public class Demo6 {
	public static void main(String[] args) throws Exception{
		//1创建BufferedWriter对象
		FileWriter fw=new FileWriter("d:\\buffer.txt");
		BufferedWriter bw=new BufferedWriter(fw);
		//2写入
		for(int i=0;i<10;i++) {
			bw.write("好好学习，天天向上");
			bw.newLine();//写入一个换行符 windows \r\n  linux  \n    
			bw.flush();
		}
		//3关闭
		bw.close();
		System.out.println("执行完毕");

	}
}
