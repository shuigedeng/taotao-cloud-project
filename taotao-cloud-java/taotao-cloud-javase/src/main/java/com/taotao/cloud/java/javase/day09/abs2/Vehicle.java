package com.taotao.cloud.java.javase.day09.abs2;
/**
 * 交通工具类
 * @author shuigedeng
 *
 */
public abstract class Vehicle {
	private String brand;
	public Vehicle() {
		// TODO Auto-generated constructor stub
	}
	public Vehicle(String brand) {
		super();
		this.brand = brand;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
	//前进
	public abstract void run();
	
}
