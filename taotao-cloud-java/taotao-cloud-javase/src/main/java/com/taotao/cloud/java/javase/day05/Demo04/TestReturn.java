package com.taotao.cloud.java.javase.day05.Demo04;

public class TestReturn{

	public static void main(String[] args){

		int result = add(5,10);// int result = 15;

		System.out.println(result);

		//在计算了5+10的总和之后，继续与20进行相加求和


		int result2 = add( result , 20);

		System.out.println(result2);

	}

	public static int add(int num1 , int num2){
		int sum = num1 + num2;
		return sum;
	}


}
