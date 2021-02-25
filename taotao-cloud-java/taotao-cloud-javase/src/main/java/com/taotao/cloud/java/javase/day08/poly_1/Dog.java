package com.taotao.cloud.java.javase.day08.poly_1;

public class Dog extends Animal{
	//毛色
	String furColor;
	
	//跑
	public void run() {
		System.out.println("狗狗开始跑步...");
	}
	
	@Override
	public void eat() {
		System.out.println("狗狗开始大口大口的吃狗粮");
	}
}
