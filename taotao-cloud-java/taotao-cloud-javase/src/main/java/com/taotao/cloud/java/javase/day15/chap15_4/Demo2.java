package com.taotao.cloud.java.javase.day15.chap15_4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;

/**
 * 演示Properties集合的使用
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) throws Exception {
		//1创建集合
		Properties properties=new Properties();
		//2添加数据
		properties.setProperty("username", "zhangsan");
		properties.setProperty("age", "20");
		System.out.println(properties.toString());
		//3遍历
		//3.1-----keySet----
		//3.2-----entrySet----
		//3.3-----stringPropertyNames()---
		Set<String> pronames=properties.stringPropertyNames();
		for (String pro : pronames) {
			System.out.println(pro+"====="+properties.getProperty(pro));
		}
		//4和流有关的方法
		//----------list方法---------
//		PrintWriter pw=new PrintWriter("d:\\print.txt");
//		properties.list(pw);
//		pw.close();
		
		//----------2store方法 保存-----------
//		FileOutputStream fos=new FileOutputStream("d:\\store.properties");
//		properties.store(fos, "注释");
//		fos.close();
		
		//----------3load方法 加载-------------
		Properties properties2=new Properties();
		FileInputStream fis=new FileInputStream("d:\\store.properties");
		properties2.load(fis);
		fis.close();
		System.out.println(properties2.toString());
		
	}
}
