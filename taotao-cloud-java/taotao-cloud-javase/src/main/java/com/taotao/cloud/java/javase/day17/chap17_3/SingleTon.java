package com.taotao.cloud.java.javase.day17.chap17_3;
/**
 * 饿汉式单例
 * （1）首先创建一个常量
 * （2）构造方法改成私有的，类外部不能创建对象
 * （3）通过一个公开的方法，返回这个对象
 * 
 * 优点：线程安全，缺点:声明周期太长，浪费空间
 * @author wgy
 *
 */
public class SingleTon {
	private static final SingleTon instance=new SingleTon();
	private SingleTon() {}
	public static SingleTon getInstance() {
		return instance;
	}
}
