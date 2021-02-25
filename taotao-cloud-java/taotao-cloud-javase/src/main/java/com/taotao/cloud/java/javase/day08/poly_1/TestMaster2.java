package com.taotao.cloud.java.javase.day08.poly_1;

import java.util.Scanner;

public class TestMaster2 {
	public static void main(String[] args) {
		System.out.println("--------欢迎来到xxx动物市场--------");
		System.out.println("----------1.狗狗  2.鸟儿------------");
		System.out.println("请选择");
		Scanner input=new Scanner(System.in);
		int choice=input.nextInt();
		Master master=new Master();
		Animal animal=master.buy(choice);
		if(animal!=null) {
			System.out.println("购买成功");
			if(animal instanceof Dog) {
				Dog dog=(Dog)animal;
				dog.run();
			}else if(animal instanceof Bird) {
				Bird bird=(Bird)animal;
				bird.fly();
			}
		}else {
			System.out.println("购物失败");
		}
	}
}
