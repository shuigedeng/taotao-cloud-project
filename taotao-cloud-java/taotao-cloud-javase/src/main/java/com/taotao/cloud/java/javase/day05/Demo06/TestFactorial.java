package com.taotao.cloud.java.javase.day05.Demo06;

public class TestFactorial{

	public static void main(String[] args){

		//计算阶乘
		int result = factorial(4);

		System.out.println(result);

	}

	//计算n的阶乘
	public static int factorial(int n){

		if(n == 1){
			return 1;
		}

		return n * factorial( n - 1 );
	}

	/*
	//计算5的阶乘
	public static int getFive(int n){//n = 5
		//逻辑代码
		return n * getFour(n-1);
		//return 5 * 24;
	}

	//计算4的阶乘
	public static int getFour(int n){//n = 4
		return n * getThree(n-1);
		//return 4 * 6;
	}
	
	public static int getThree(int n){//n = 3
		return n * getTwo(n-1);
		//return 3 * 2;
	}
	
	public static int getTwo(int n){//n = 2
		return n * getOne(n-1);
		//return 2 * 1;
	}
	
	
	public static int getOne(int n){//n = 1
		return 1;
	}
	*/

}
