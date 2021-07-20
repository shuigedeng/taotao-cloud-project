package com.taotao.cloud.java.javase.day12.chapter12_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * ArrayList的使用
 * 存储结构:数组，查找遍历速度块，增删慢
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) {
		//创建集合  size 0  容量 0，扩容原来的1.5倍
		ArrayList arrayList=new ArrayList<>();
		//1添加元素
		Student s1=new Student("刘德华", 20);
		Student s2=new Student("郭富城", 22);
		Student s3=new Student("梁朝伟", 18);
		arrayList.add(s1);
		arrayList.add(s2);
		arrayList.add(s3);
		System.out.println("元素个数:"+arrayList.size());
		System.out.println(arrayList.toString());
		//2删除元素
//		arrayList.remove(new Student("刘德华", 20));//equals(this==obj)
//		System.out.println("删除之后:"+arrayList.size());
		
		//3遍历元素【重点】
		//3.1使用迭代器
		System.out.println("-------3.1使用迭代器-----");
		Iterator it=arrayList.iterator();
		while(it.hasNext()) {
			Student s=(Student)it.next();
			System.out.println(s.toString());
		}
		//3.2列表迭代器
		ListIterator lit=arrayList.listIterator();
		System.out.println("-------3.2使用列表迭代器---------");
		while(lit.hasNext()) {
			Student s=(Student)lit.next();
			System.out.println(s.toString());
		}
		
		System.out.println("-------3.2使用列表迭代器逆序---------");
		while(lit.hasPrevious()) {
			Student s=(Student)lit.previous();
			System.out.println(s.toString());
		}
		
		//4判断
		System.out.println(arrayList.contains(new Student("梁朝伟", 18)));
		System.out.println(arrayList.isEmpty());
		
		//5查找
		System.out.println(arrayList.indexOf(new Student("梁朝伟", 18)));
	}
}
