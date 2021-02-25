package com.taotao.cloud.java.javase.day05.Demo05;

public class TestReturn2{

	public static void main(String[] args){

		//double result = calc(1.5 , 10.2);

		//System.out.println("计算结果：" + result);

		//-------------------------------------------

		//String str = isEven(10);

		//System.out.println(str);

		//-------------------------------------------

		show();


	}


	public static double calc(double a , double b){
		double sum = a + b;
		System.out.println("运算结束");
		return sum;//结束当前方法，并伴有返回值，返回到方法调用处
	}

	public static String isEven(int num){
		if(num % 2 == 0){
			return "偶数";
		}else{
			return "奇数";
		}
	}

	public static void show(){
		for(int i = 1 ; i <= 10 ; i++){
			System.out.println("当前值" + i);
			if(i == 5){
				return;//结束当前方法，返回到方法调用出
			}
		}
		System.out.println("show() .............");
	}
}
