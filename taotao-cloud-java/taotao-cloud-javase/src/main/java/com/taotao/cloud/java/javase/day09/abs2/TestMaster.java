package com.taotao.cloud.java.javase.day09.abs2;

public class TestMaster {
	public static void main(String[] args) {
		Master xiaoming=new Master("小明");
		Vehicle car=new Car("宝马");
		Vehicle yongjiu=new Bike("永久");
		xiaoming.goHome(yongjiu);
		
	}
}
