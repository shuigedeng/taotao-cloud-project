package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * 使用ObjectInputStream实现反序列化(读取重构成对象)
 * @author shuigedeng
 *
 */
public class Demo7 {
	public static void main(String[] args) throws Exception {
		//1创建对象流
		FileInputStream fis=new FileInputStream("d:\\stu.bin");
		ObjectInputStream ois=new ObjectInputStream(fis);
		//2读取文件(反序列化)
//		Student s=(Student)ois.readObject();
//		Student s2=(Student)ois.readObject();
		ArrayList<Student> list=(ArrayList<Student>)ois.readObject();
		//3关闭
		ois.close();
		System.out.println("执行完毕");
//		System.out.println(s.toString());
//		System.out.println(s2.toString());
		System.out.println(list.toString());
	}
}
