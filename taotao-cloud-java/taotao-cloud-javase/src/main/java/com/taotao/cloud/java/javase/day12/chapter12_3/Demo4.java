package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * TreeSet的使用
 * 存储结构:红黑树 
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		//创建集合
		TreeSet<String> treeSet=new TreeSet<>();
		//1添加元素
		treeSet.add("xyz");
		treeSet.add("abc");
		treeSet.add("hello");
		treeSet.add("xyz");
		System.out.println("元素个数:"+treeSet.size());
		System.out.println(treeSet.toString());
		
		//2删除
//		treeSet.remove("xyz");
//		System.out.println("删除之后:"+treeSet.size());
		//3遍历
		//3.1使用增强for
		for (String string : treeSet) {
			System.out.println(string);
		}
		System.out.println("-----------");
		//3.2使用迭代器
		Iterator<String> it=treeSet.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//4判断
		System.out.println(treeSet.contains("abc"));
	}
}
