package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.HashSet;
import java.util.Iterator;

/**
 * HashSet集合的使用
 * 存储结构:哈希表(数组+链表+红黑树)
 * 
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		//新建集合
		HashSet<String> hashSet=new HashSet<String>();
		//1添加元素
		hashSet.add("刘德华");
		hashSet.add("梁朝伟");
		hashSet.add("林志玲");
		hashSet.add("周润发");
		//hashSet.add("刘德华");
		System.out.println("元素个数:"+hashSet.size());
		System.out.println(hashSet.toString());
		//2删除数据
//		hashSet.remove("刘德华");
//		System.out.println("删除之后:"+hashSet.size());
		//3遍历操作
		//3.1增强for
		System.out.println("--------3.1增强for--------");
		for (String string : hashSet) {
			System.out.println(string);
		}
		//3.2使用迭代器
		System.out.println("-------3.2迭代器--------");
		Iterator<String> it=hashSet.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//4判断
		System.out.println(hashSet.contains("郭富城"));
		System.out.println(hashSet.isEmpty());
		
	}
}
