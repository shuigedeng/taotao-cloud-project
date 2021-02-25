package com.taotao.cloud.java.javase.day09.abs2;

public class Car extends Vehicle{
	public Car() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Car(String brand) {
		super(brand);
	}

	@Override
	public void run() {
		System.out.println(super.getBrand()+"牌的汽车正在前进");
	}
}
