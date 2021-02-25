package com.taotao.cloud.java.javase.day02.Demo11;

public class TestOperation1{

	public static void main(String[] args){

		int a = 10;

		int b = 3;

		System.out.println( a / b );//求商 = 3

		System.out.println( a % b );//求余 = 1



		double d = 10.0;

		int c = 3;

		System.out.println(d / c);//求商 3.33.......




		int num1 = 10;

		num1++;//自增1

		System.out.println(num1);



		int num2 = 10;

		num2--;//自减1

		System.out.println(num2);



		int num3 = 5;

		//前++ ：先++，再打印自增后的值

		//后++ ： 先打印当前值，再++

		System.out.println( ++num3 );

		System.out.println( num3 );


		int num4 = 100;

		//前++ ：先++，再赋值

		//后++ ： 先赋值，再++

		int num5 = num4++;

		System.out.println(num5);

		System.out.println(num4);


	}
}
