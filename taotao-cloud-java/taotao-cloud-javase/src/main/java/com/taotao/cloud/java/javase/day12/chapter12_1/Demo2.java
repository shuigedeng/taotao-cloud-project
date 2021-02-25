package com.taotao.cloud.java.javase.day12.chapter12_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Collection的使用:保存学生信息
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		//新建Collection对象
		Collection collection=new ArrayList();
		Student s1=new Student("张三", 20);
		Student s2=new Student("张无忌", 18);
		Student s3=new Student("王二", 22);
		//1添加数据
		collection.add(s1);
		collection.add(s2);
		collection.add(s3);
		System.out.println("元素个数:"+collection.size());
		System.out.println(collection.toString());
		//2删除
		//collection.remove(s1);
		//collection.remove(new Student("王二", 22));
//		collection.clear();
//		System.out.println("删除之后:"+collection.size());
		//3遍历
		//3.1 增强for
		System.out.println("-------增强for----");
		for (Object object : collection) {
			Student s=(Student)object;
			System.out.println(s.toString());
		}
		//3.2迭代器: hasNext()  next();  remove(); 迭代过程中不能使用使用collection的删除方法
		System.out.println("-------迭代器----");
		Iterator it=collection.iterator();
		while(it.hasNext()) {
			Student s=(Student)it.next();
			System.out.println(s.toString());
		}
		//4判断
		System.out.println(collection.contains(s1));
		System.out.println(collection.isEmpty());
		
	}
}
