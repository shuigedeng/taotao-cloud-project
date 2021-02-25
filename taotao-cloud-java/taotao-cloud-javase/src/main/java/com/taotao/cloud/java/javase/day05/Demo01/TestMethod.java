package com.taotao.cloud.java.javase.day05.Demo01;

public class TestMethod{

	public static void main(String[] args){

		System.out.println("床前明月光");

		printSign();//对方法的调用

		System.out.println("疑是地上霜");

		printSign();

		System.out.println("举头望明月");

		printSign();

		System.out.println("低头思故乡");

		printSign();

	}

	//自定义方法（在一行中输入10个减号，代表分隔符）
	public static void printSign(){

		for(int i = 1 ; i <= 10 ; i++){
			System.out.print("-");
		}
		System.out.println();

	}


}
