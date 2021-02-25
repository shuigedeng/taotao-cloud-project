package com.taotao.cloud.java.javase.day02.Demo11;

public class TestOperation5{

	public static void main(String[] args){

		//1.判断
		//2.赋值
		//布尔表达式 ? 值1 : 值2

		int javaScore = 100;

		String result = javaScore == 100 ? "恭喜" : "加油" ;

		System.out.println(result);


		int webScore = 99;

		int result2 = webScore == 100 ? 666 : 111;

		System.out.println(result2);

	}
}
