package com.taotao.cloud.java.javase.day04.Demo03;

import java.util.Scanner;

public class TestDoWhile2{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		//循环操作：学生抄写代码、教师输入评语

		char answer = 'y';//赋予初始值

		do{
			System.out.println("抄写一遍...");

			System.out.println("请教师输入评语：");

			answer = input.next().charAt(0);//控制台获取'y'或者'n'
		}while( answer != 'y' );

		System.out.println("程序结束...");

	}
}
