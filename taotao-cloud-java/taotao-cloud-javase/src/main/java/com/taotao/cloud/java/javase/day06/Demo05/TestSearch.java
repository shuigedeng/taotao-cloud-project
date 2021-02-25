package com.taotao.cloud.java.javase.day06.Demo05;

import java.util.Scanner;

public class TestSearch{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);


		System.out.println("请输入一个整数：");

		int n = input.nextInt();


		int[] numbers = new int[]{1,2,3,4,5,6,7};

		int index = -1;//代表n从未出现在数组中

		//循环查找的过程
		for(int i = 0 ; i < numbers.length ; i++){
			if(numbers[i] == n){
				//存在
				index = i;//改变index，代表n所出现的下标
				break;
			}
		}

		System.out.println(index);

	}
}
