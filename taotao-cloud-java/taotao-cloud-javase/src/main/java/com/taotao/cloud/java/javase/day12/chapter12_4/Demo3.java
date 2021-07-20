package com.taotao.cloud.java.javase.day12.chapter12_4;

import java.util.Map;
import java.util.TreeMap;

/**
 * TreeMap的使用
 * 存储结构：红黑树
 * @author shuigedeng
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		
		//新建集合(定制比较)
		TreeMap<Student, String> treeMap=new TreeMap<Student,String>();
		//1添加元素
		Student s1=new Student("孙悟空", 100);
		Student s2=new Student("猪八戒", 101);
		Student s3=new Student("沙和尚", 102);
		treeMap.put(s1, "北京");
		treeMap.put(s2, "上海");
		treeMap.put(s3, "深圳");
		treeMap.put(new Student("沙和尚", 102), "南京");
		System.out.println("元素个数:"+treeMap.size());
		System.out.println(treeMap.toString());
		//2删除
//		treeMap.remove(new Student("猪八戒", 101));
//		System.out.println(treeMap.size());
		//3遍历
		//3.1使用keySet
		System.out.println("-----keySet()-------");
		for (Student key : treeMap.keySet()) {
			System.out.println(key+"-------"+treeMap.get(key));
		}
		System.out.println("------entrySet()--------");
		for(Map.Entry<Student, String> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey()+"--------"+entry.getValue());
		}
		
		//4判断
		System.out.println(treeMap.containsKey(new Student("沙和尚", 102)));
	}
}
