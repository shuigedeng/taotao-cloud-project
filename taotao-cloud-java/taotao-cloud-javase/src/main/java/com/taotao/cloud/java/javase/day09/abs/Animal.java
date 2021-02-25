package com.taotao.cloud.java.javase.day09.abs;
/**
 * 抽象动物类
 * @author wgy
 *
 */
public abstract class Animal {
	
	String breed;//品种
	int age;//年龄
	String sex;//性别
	
	//吃(抽象方法)
	public abstract void eat();
	//睡
	public void sleep() {
		System.out.println("动物睡...");
	}
}
