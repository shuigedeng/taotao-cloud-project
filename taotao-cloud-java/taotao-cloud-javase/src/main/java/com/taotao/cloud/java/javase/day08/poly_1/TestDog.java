package com.taotao.cloud.java.javase.day08.poly_1;

public class TestDog {
	public static void main(String[] args) {
//		Animal animal=new Animal();
		//1逻辑上 Dog is a Animal  2 语法讲 (自动类型转换)
		Animal a=new Dog();
		
		a.age=2;
		a.breed="萨摩";
		a.sex="公";
		
		a.eat();
		a.sleep();
		
		System.out.println("-----------------");
		
		Animal a2=new Bird();
		a2.eat();
		a2.sleep();
		
		
//		short s=10;
//		int num=s;
	}
}
