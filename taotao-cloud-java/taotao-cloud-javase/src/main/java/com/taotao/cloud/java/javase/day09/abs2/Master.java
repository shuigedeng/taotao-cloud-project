package com.taotao.cloud.java.javase.day09.abs2;

public class Master {
	private String name;
	public Master() {
		// TODO Auto-generated constructor stub
	}
	public Master(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//回家
	public void goHome(Vehicle vehicle) {
		System.out.println(this.name+"下班回家了...");
		vehicle.run();
	}
	
}
