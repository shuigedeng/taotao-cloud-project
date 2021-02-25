package com.taotao.cloud.java.javase.day09.final_1;
/**
 * final修饰类：最终类，不能被继承
 * @author wgy
 *
 */
public  class Car {
	final String brand;//实例常量，不再提供默认值，必须赋值，并且只能赋值一次
	final static String ADDRESS;//静态常量，不再提供默认值，必须赋值，并且只能赋值一次
	static {
		ADDRESS="美国";
	}
	
	public Car() {
		this.brand="宝马";
	}
	public Car(String brand) {
		this.brand=brand;
	}
	
	String color;
	//final方法，最终方法，不能被重写覆盖，但是可以被继承
	public final void run() {
		System.out.println("汽车正在前进...");
		final int num=100;
		//num=200;
		final String city="北京";
		//city="上海";
		//brand="奔驰";
	}
}
