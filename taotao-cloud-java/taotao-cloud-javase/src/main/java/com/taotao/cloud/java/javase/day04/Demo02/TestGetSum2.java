package com.taotao.cloud.java.javase.day04.Demo02;

public class TestGetSum2{

	public static void main(String[] args){

		//求1~100之间所有偶数的和


		//方案一：获取所有的偶数，相加求和


		//方案二：依旧获取1~100之间的每一个数字，进行偶数的判断，满足条件之后，相加求和

		int i = 1;

		int sum = 0;

		while( i <= 100 ){

			if( i % 2 != 0 ){//判断偶数
				//求和
				sum = sum + i;
			}

			i++;
		}

		System.out.println("偶数的和：" + sum);


	}
}
