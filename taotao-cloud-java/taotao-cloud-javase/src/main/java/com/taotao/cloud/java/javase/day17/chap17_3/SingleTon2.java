package com.taotao.cloud.java.javase.day17.chap17_3;

/**
 * 懒汉式单例 
 * （1）首先创建一个对象，赋值为null 
 * （2）构造方法改成私有的，类外部不能创建对象 
 * （3）通过一个公开的方法，返回这个对象
 * 
 * 优点:声明周期短，节省空间 缺点：有线程安全问题
 * @author wgy
 *
 */
public class SingleTon2 {
	// 创建对象
	private static SingleTon2 instance = null;

	// 私有化构造方法
	private SingleTon2() {
	}

	// 静态方法
	public static  SingleTon2 getInstance() {
		if(instance==null) {//提高执行效率
			synchronized (SingleTon2.class) {
				if (instance == null) {
					instance = new SingleTon2();
				}
			}
		}
		return instance;
	}
}
