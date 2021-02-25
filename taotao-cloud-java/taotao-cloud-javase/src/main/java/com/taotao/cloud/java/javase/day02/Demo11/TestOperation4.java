package com.taotao.cloud.java.javase.day02.Demo11;

public class TestOperation4{

	public static void main(String[] args){

		int javaScore = 100;

		int webScore = 99;

		//比较两者是否相等
		System.out.println( javaScore == webScore);


		//别分判断二者是否为满分
		System.out.println( javaScore == 100);
		System.out.println( webScore == 100);


		//一次性判断二者是否均为满分

		//															false
		//										true				&&				false     两个表达式同时为真
		System.out.println( javaScore == 100  && webScore == 100 );



		//一次性判断二者是是否有一个满分

		//														true
		//											true			||			false
		System.out.println( javaScore == 100  ||  webScore == 100 );



		boolean result = javaScore == 100;

		//Java的成绩是满分吗？
		System.out.println(result);//true

		//Java的成绩不是满分吗？
		System.out.println( !result );//false

	}
}



