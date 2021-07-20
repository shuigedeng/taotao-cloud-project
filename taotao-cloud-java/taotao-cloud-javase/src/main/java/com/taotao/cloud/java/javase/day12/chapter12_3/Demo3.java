package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.HashSet;
import java.util.Iterator;

/**
 * HashSet的使用
 * 存储结构:哈希表(数组+链表+红黑树)
 * 存储过程(重复依据)
 * (1)根据hashcode计算保存的位置，如果此位置为空，则直接保存，如果不为空执行第二步。
 * (2)再执行equals方法，如果equals方法为true，则认为是重复，否则，形成链表
 * @author shuigedeng
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		//创建集合
		HashSet<Person> persons=new HashSet<>();
		//1添加数据
		Person p1=new Person("刘德华", 20);
		Person p2=new Person("林志玲", 22);
		Person p3=new Person("梁朝伟", 25);
		
		persons.add(p1);
		persons.add(p2);
		persons.add(p3);
		//persons.add(p3);重复
		persons.add(new Person("梁朝伟", 25));
		
		
		System.out.println("元素个数:"+persons.size());
		System.out.println(persons.toString());
		
		//2删除操作
		//persons.remove(p1);
//		persons.remove(new Person("刘德华", 20));
//		System.out.println("删除之后:"+persons.size());
		
		//3遍历[重点]

		//3.1使用增强for
		for (Person person : persons) {
			System.out.println(person.toString());
		}
		System.out.println("--------------");
		//3.2迭代器
		Iterator<Person> it=persons.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//4判断
		System.out.println(persons.contains(new Person("刘德华", 20)));
		System.out.println(persons.isEmpty());
	}
}
