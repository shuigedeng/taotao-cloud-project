package com.taotao.cloud.java.javase.day09.static_1;

public class TestStudent {
	public static void main(String[] args) {
		Student s1=new Student();
		s1.name="aaa";
		s1.age=20;

		
		Student s2=new Student();
		s2.name="bbb";
		s2.age=22;
		
		s1.show();
		s2.show();
		//---------------调用静态属性 类名.静态属性名----------
		Student.count=50;
		s1.count=100;
		System.out.println("学生数量："+Student.count);
		//---------------调用静态方法 类名.静态方法名----------
		System.out.println("================================");
		Student.method1();
		Student.method2();
		
		
	}
}
