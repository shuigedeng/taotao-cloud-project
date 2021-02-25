package com.taotao.cloud.java.javase.day12.chapter12_4;

import java.util.HashMap;
import java.util.Map;

import com.qf.chapter12_3.Person;

/**
 * HashMap集合的使用
 * 存储结构:哈希表(数组+链表+红黑树)
 * 使用key可hashcode和equals作为重复
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) {
	
		//创建集合
		HashMap<Student, String> students=new HashMap<Student,String>();
		//刚创建hashmap之后没有添加元素 table=null   size=0 目的节省空间
		//1添加元素
		Student s1=new Student("孙悟空", 100);
		Student s2=new Student("猪八戒", 101);
		Student s3=new Student("沙和尚", 102);
		students.put(s1, "北京");
		students.put(s2, "上海");
		students.put(s3, "杭州");
		//students.put(s3, "南京");
		students.put(new Student("沙和尚", 102), "杭州");
		System.out.println("元素个数:"+students.size());
		System.out.println(students.toString());
		//2删除
//		students.remove(s1);
//		System.out.println("删除之后"+students.size());
		//3遍历
		System.out.println("--------keySet---------");
		//3.1使用keySet();
		for (Student key : students.keySet()) {
			System.out.println(key.toString()+"========="+students.get(key));
		}
		System.out.println("--------entrySet---------");
		//3.2使用entrySet();
		for (Map.Entry<Student, String> entry : students.entrySet()) {
			System.out.println(entry.getKey()+"---------"+entry.getValue());
		}
		//4判断
		System.out.println(students.containsKey(new Student("孙悟空", 100)));
		System.out.println(students.containsValue("杭州"));
		
	}
}
