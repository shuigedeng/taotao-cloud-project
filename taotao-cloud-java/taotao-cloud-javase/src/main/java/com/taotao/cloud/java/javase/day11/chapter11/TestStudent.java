package com.taotao.cloud.java.javase.day11.chapter11;

public class TestStudent {
	public static void main(String[] args) {
		//1getClass方法
		System.out.println("------------1getClass---------");
		Student s1=new Student("aaa", 20);
		Student s2=new Student("bbb",22);
		//判断s1和s2是不是同一个类型
		Class class1=s1.getClass();
		Class class2=s2.getClass();
		if(class1==class2) {
			System.out.println("s1和s2属于同一个类型");
		}else {
			System.out.println("s1和s2不属于同一个类型");
		}
		System.out.println("-----------2hashCode------------");
		//2hashCode方法
		System.out.println(s1.hashCode());
		
		System.out.println(s2.hashCode());
		Student s3=s1;
		System.out.println(s3.hashCode());
		
		//3toString方法
		System.out.println("-----------3toString------------");
		System.out.println(s1.toString());
		System.out.println(s2.toString());
		
		//4equals方法：判断两个对象是否相等
		System.out.println("-----------4equals------------");
		System.out.println(s1.equals(s2));
		
		Student s4=new Student("小明", 17);
		Student s5=new Student("小明",17);
		System.out.println(s4.equals(s5));
		
		
	}
}
