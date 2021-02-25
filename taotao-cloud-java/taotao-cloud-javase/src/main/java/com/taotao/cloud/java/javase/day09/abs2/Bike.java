package com.taotao.cloud.java.javase.day09.abs2;

public class Bike extends Vehicle{
	public Bike() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Bike(String brand) {
		super(brand);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void run() {
		System.out.println(super.getBrand()+"牌的自行车正在前进...");
	}
}
