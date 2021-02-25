package com.taotao.cloud.java.javase.day05.Demo03;

public class TestMethod3{

	public static void main(String[] args){

		//需求：在多次调用printSign方法时，可以打印不同次数的符号

		System.out.println("床前明月光");

		printSign(5 , '-');

		System.out.println("疑是地上霜");

		printSign(10 , '#');

		System.out.println("举头望明月");

		printSign(15 , '*');

		System.out.println("低头思故乡");

		printSign(20 , '+');

	}

	public static void printSign(int count , char sign){

		for(int i = 1 ; i <= count ; i++){
			System.out.print(sign);
		}
		System.out.println();

	}

}
