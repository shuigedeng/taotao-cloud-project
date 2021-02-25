package com.taotao.cloud.java.javase.day04.Demo07;

public class TestNestedFor3{

	public static void main(String[] args){

		//打印直角三角形

		/*

		*			j <= 1
		**			j <= 2
		***		j <= 3
		****		j <= 4
		*****		j <= 5

		-----------------------

		*
		**
		***
		****
		*****

		*/

		//外层控制行数
		for(int i = 1 ; i <= 5 ; i++){ // i = 6

			//内层控制列数
			for(int j = 1 ; j <= i ; j++){ //
				System.out.print("*");
			}
			System.out.println();

		}

		System.out.println("程序结束");

	}
}
