package com.taotao.cloud.java.javase.day12.chapter12_2;

import com.taotao.cloud.java.javase.day12.chapter12_1.Student;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * LinkedList的使用
 * 存储结构:双向链表
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		//创建集合
		LinkedList linkedList=new LinkedList();
		//1添加元素
		Student s1=new Student("刘德华", 20);
		Student s2=new Student("郭富城", 22);
		Student s3=new Student("梁朝伟", 18);
		linkedList.add(s1);
		linkedList.add(s2);
		linkedList.add(s3);
		
		System.out.println("元素个数:"+linkedList.size());
		System.out.println(linkedList.toString());
		
		//2删除
//		linkedList.remove(new Student("刘德华", 20));
//		System.out.println("删除之后:"+linkedList.size());
//		linkedList.clear();
		//3遍历
		//3.1for遍历
		System.out.println("-----for----------");
		for(int i=0;i<linkedList.size();i++) {
			System.out.println(linkedList.get(i));
		}
		//3.2增强for
		System.out.println("-----增强for----------");
		for (Object object : linkedList) {
			Student s=(Student)object;
			System.out.println(s.toString());
		}
		//3.3使用迭代器
		System.out.println("-----使用迭代器----------");
		Iterator it=linkedList.iterator();
		while(it.hasNext()) {
			Student s=(Student)it.next();
			System.out.println(s.toString());
		}
		//3.4-使用列表迭代器
		System.out.println("-----使用列表迭代器----------");
		ListIterator lit=linkedList.listIterator();
		while(lit.hasNext()) {
			Student s=(Student)lit.next();
			System.out.println(s.toString());
		}
		
		//4判断
		System.out.println(linkedList.contains(s1));
		System.out.println(linkedList.isEmpty());
		//5获取
		System.out.println(linkedList.indexOf(s2));
	}
}
