package com.taotao.cloud.java.javase.day11.chapter11_3;

//外部类
public class Outer {
	private String name="刘德华";
	private int age=35;
	
	public  void show(final int i) {
		//定义局部变量
		String address="深圳";
		//局部内部类 :注意不能加任何访问修饰符
		class Inner{
			//局部内部类的属性
			private String phone="15588888888";
			private String email="liudehua@qq.com";
			//private final static int count=2000;
			
			public void show2() {
				//访问外部类的属性
				System.out.println(Outer.this.name);
				System.out.println(Outer.this.age);
				//访问内部类的属性
				System.out.println(this.phone);
				System.out.println(this.email);
				//访问局部变量,jdk1.7要求：变量必须是常量 final，jdk1.8 自动添加final
				System.out.println("深圳");
				System.out.println(i);
			}	
		}
		//创建局部内部类对象
		Inner inner=new Inner();
		inner.show2();
		
	}
	
	

}
