package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 测试Set接口的使用
 * 特点：（1）无序、没有下标 （2）不能重复
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) {
		//创建集合
		Set<String> set=new HashSet<>();
		//1添加数据
		set.add("小米");
		set.add("苹果");
		set.add("华为");
		//set.add("华为");
		System.out.println("数据个数:"+set.size());
		System.out.println(set.toString());
		//2删除数据
//		set.remove("小米");
//		System.out.println(set.toString());
		//3遍历 【重点】
		//3.1使用增强for
		System.out.println("-----增强for-------");
		for (String string : set) {
			System.out.println(string);
		}
		//3.2使用迭代器
		System.out.println("-----使用迭代器------");
		Iterator<String> it=set.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//4判断
		System.out.println(set.contains("华为"));
		System.out.println(set.isEmpty());
	}
}
