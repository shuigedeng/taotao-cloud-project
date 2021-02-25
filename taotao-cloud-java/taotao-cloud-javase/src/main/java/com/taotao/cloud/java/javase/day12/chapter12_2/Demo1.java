package com.taotao.cloud.java.javase.day12.chapter12_2;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 演示Vector集合的使用
 * 存储结构:数组
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) {
		//创建集合
		Vector vector=new Vector<>();
		//1添加元素
		vector.add("草莓");
		vector.add("芒果");
		vector.add("西瓜");
		System.out.println("元素个数:"+vector.size());
		//2删除
//		vector.remove(0);
//		vector.remove("西瓜");
//		vector.clear();
		//3遍历
		//使用枚举器
		Enumeration en=vector.elements();
		while(en.hasMoreElements()) {
			String  o=(String)en.nextElement();
			System.out.println(o);
		}
		//4判断
		System.out.println(vector.contains("西瓜"));
		System.out.println(vector.isEmpty());
		//5vector其他方法
		//firsetElement、lastElement、elementAt();
		
		
		
		
	}
}
