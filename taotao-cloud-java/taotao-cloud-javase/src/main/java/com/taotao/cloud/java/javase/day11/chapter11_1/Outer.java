package com.taotao.cloud.java.javase.day11.chapter11_1;
//外部类
public class Outer {
	//实例变量
	private String name="张三";
	private int age=20;
	
	//内部类
	class Inner{
		private String address="北京";
		private String phone="110";
		//
		private String name="李四";
		
		//private static final String country="中国";
		//方法
		public void show() {
			//打印外部类的属性，内部类属性和外部类的属性名字相同Outer.this
			System.out.println(Outer.this.name);
			System.out.println(Outer.this.age);
			//打印内部类中的属性
			System.out.println(this.address);
			System.out.println(this.phone);
		}
	}
	
}
