package com.taotao.cloud.java.javase.day10.interface_3;

public class TestDog {
	public static void main(String[] args) {
		Dog wangcai=new Dog();
		Animal a=wangcai;
		Runnable runnable=wangcai;
		Swimable swimable=wangcai;
		
		//--------------------
		
		swimable.swim();
		
	}
}
