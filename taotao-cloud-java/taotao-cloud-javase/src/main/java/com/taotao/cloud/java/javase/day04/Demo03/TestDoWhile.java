package com.taotao.cloud.java.javase.day04.Demo03;

public class TestDoWhile{

	public static void main(String[] args){

		//打印100遍“HelloWorld”

		int i = 1;

		do{
			System.out.println("HelloWorld" + i);
			i++;
		}while( i <= 100);

		System.out.println("程序结束...");

	}
}
