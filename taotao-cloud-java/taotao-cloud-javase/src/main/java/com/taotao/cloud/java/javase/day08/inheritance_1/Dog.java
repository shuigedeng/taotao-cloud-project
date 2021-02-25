package com.taotao.cloud.java.javase.day08.inheritance_1;
/**
 * 狗狗类
 * @author wgy
 *
 */
public class Dog extends Animal{
	
	//毛色
	String furColor;
	
	//方法重写
//	public void eat() {
//		System.out.println("狗狗大口大口的吃狗粮...");
//	}
	@Override//注解：验证是否遵循重写的规则
	public void eat() {
		System.out.println("狗狗大口大口的吃狗粮...");
	}

	//跑
	public void run() {
		System.out.println("跑........");
	}
}
