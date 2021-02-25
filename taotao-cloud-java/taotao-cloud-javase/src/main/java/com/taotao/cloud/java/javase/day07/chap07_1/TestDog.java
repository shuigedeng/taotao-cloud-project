package com.taotao.cloud.java.javase.day07.chap07_1;

public class TestDog {
	public static void main(String[] args) {
		//创建一个对象,对象名myDog
		Dog myDog=new Dog();
		//属性赋值
		myDog.breed="萨摩";
		myDog.age=2;
		myDog.sex="公";
		myDog.furColor="白色";
		//获取属性值
		System.out.println(myDog.breed);
		System.out.println(myDog.age);
		System.out.println(myDog.sex);
		System.out.println(myDog.furColor);
		//调用方法
		myDog.eat();
		myDog.sleep();
		
		System.out.println("--------------");
		
		//创建对象
		Dog myDog2=new Dog();
		System.out.println(myDog2.breed);
		System.out.println(myDog2.age);
		System.out.println(myDog2.sex);
		System.out.println(myDog2.furColor);
	}
}
