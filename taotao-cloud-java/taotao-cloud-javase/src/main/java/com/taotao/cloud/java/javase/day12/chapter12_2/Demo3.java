package com.taotao.cloud.java.javase.day12.chapter12_2;

import java.util.ArrayList;
import java.util.Iterator;

import com.qf.chapter12_1.Student;

public class Demo3 {
	public static void main(String[] args) {
		ArrayList<String> arrayList=new ArrayList<String>();
		arrayList.add("xxx");
		arrayList.add("yyy");
//		arrayList.add(10);
//		arrayList.add(20);
		
		for (String string : arrayList) {
			System.out.println(string);
		}
		
		ArrayList<Student> arrayList2=new ArrayList<Student>();
		Student s1=new Student("刘德华", 20);
		Student s2=new Student("郭富城", 22);
		Student s3=new Student("梁朝伟", 18);
		arrayList2.add(s1);
		arrayList2.add(s2);
		arrayList2.add(s3);
		
		Iterator<Student> it=arrayList2.iterator();
		while(it.hasNext()) {
			Student s=it.next();
			System.out.println(s.toString());
		}
		
	
	
	}
}
