package com.taotao.cloud.java.javase.day03.Demo03;

public class TestMultipleIf{

	public static void main(String[] args){
		
		/*
			根据预算金额选购车辆

			预算 > 100万   奔驰S级
			预算 > 50万     宝马5系
			预算 > 10万     奥迪A4L
			预算 < 10万	  捷安特自行车
		*/

		int money = 110; //单位：万

		if(money > 100){
			System.out.println("奔驰S级");
		}else if(money > 50){
			System.out.println("宝马5系");
		}else if(money > 10){
			System.out.println("奥迪A4L");
		}else{
			System.out.println("捷安特自行车");
		}

		System.out.println("程序结束...");

	}
}
