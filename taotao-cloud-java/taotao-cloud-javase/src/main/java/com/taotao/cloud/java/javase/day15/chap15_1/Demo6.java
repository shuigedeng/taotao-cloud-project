package com.taotao.cloud.java.javase.day15.chap15_1;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * 使用ObjectOutputStream实现对象的序列化
 * 注意事项：
 * (1)序列化类必须要实现Serializable接口
 * (2)序列化类中对象属性要求实现Serializable接口
 * (3)序列化版本号ID serialVersionUID,保证序列化的类和反序列化的类是同一个类
 * (4)使用transient（瞬间的）修饰属性，这个属性不能序列化
 * (5)静态属性不能被序列化
 * (6)序列化多个对象,可以借助集合实现
 * @author wgy
 *
 */
public class Demo6 {
	public static void main(String[] args) throws Exception{
		//1创建对象流
		FileOutputStream fos=new FileOutputStream("d:\\stu.bin");
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		//2序列化(写入操作)
		Student zhangsan=new Student("张三", 20);
		Student lisi=new Student("李四", 22);
		ArrayList<Student> list=new ArrayList<>();
		list.add(zhangsan);
		list.add(lisi);
		oos.writeObject(list);
	
		//3关闭
		oos.close();
		System.out.println("序列化完毕");
	}
}
