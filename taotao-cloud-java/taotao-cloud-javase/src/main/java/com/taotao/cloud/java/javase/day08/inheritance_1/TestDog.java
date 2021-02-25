package com.taotao.cloud.java.javase.day08.inheritance_1;

public class TestDog {
	public static void main(String[] args) {
		Dog wangcai=new Dog();
		wangcai.breed="萨摩";
		wangcai.age=2;
		wangcai.sex="公";
		wangcai.furColor="白";
		
		wangcai.eat();
		wangcai.sleep();
		wangcai.run();
	}
}
