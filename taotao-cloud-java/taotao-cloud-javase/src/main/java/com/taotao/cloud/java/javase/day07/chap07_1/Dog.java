package com.taotao.cloud.java.javase.day07.chap07_1;
/**
 * 狗狗类
 * @author shuigedeng
 *
 */
public class Dog {
	//属性
	//品种
	String breed;//null
	//年龄
	int age;//0
	//性别
	String sex;
	//颜色
	String furColor;
	
	//方法
	//eat吃
	public void eat() {
		int age=100;
		int n=10;
		System.out.println("狗狗正在吃东西..."+breed);
		System.out.println(n);
	}
	//sleep睡
	public void sleep() {
		int age=100;
		
		System.out.println("狗狗开始睡觉了..."+age);
	}
}
