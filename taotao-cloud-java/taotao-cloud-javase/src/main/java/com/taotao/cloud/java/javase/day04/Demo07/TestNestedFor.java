package com.taotao.cloud.java.javase.day04.Demo07;

public class TestNestedFor{

	public static void main(String[] args){

		/*

		 *****
		 *****
		 *****

		 */

		//不采用此种方式
		//System.out.println("*****");


		//冗余（重复）代码，避免重复，采取复用代码的形式




		//将内部的代码，重复3次
		for(int i = 1 ; i <= 3 ; i++){// i = 4

			//在1行中打印5颗星
			for(int j = 1 ; j <= 5 ; j++){//
				System.out.print("*");
			}
			System.out.println();

		}
		//程序流程到达此处

		/*

		 *****
		 *****
		 *****


		 */

	}
}
