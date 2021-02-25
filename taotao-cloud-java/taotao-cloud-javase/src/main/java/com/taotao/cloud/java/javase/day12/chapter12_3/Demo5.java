package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * 使用TreeSet保存数据
 * 存储结构:红黑树
 * 要求：元素必须要实现Comparable接口,compareTo()方法返回值为0,认为是重复元素
 * @author wgy
 *
 */
public class Demo5 {
	public static void main(String[] args) {
	
		//创建集合
		TreeSet<Person> persons=new TreeSet<>();
		//1添加元素
		Person p1=new Person("xyz", 20);
		Person p2=new Person("hello", 22);
		Person p3=new Person("zhangsan", 25);
		Person p4=new Person("zhangsan", 20);
		persons.add(p1);
		persons.add(p2);
		persons.add(p3);
		persons.add(p4);
		System.out.println("元素个数:"+persons.size());
		System.out.println(persons.toString());
		//2删除
//		persons.remove(p1);
//		System.out.println(persons.size());
		//3遍历
		//3.1 使用增强for
		for (Person person : persons) {
			System.out.println(person.toString());
		}
		System.out.println("------------");
		//3.2使用迭代器
		Iterator<Person> it=persons.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//4判断
		System.out.println(persons.contains(new Person("zhangsan", 20)));
	}
}
