package com.taotao.cloud.java.javase.day03.Demo06;

public class TestLocalVariable {

	public static void main(String[] args) {

		int a = 10; //局部变量的声明，必须先赋值再使用

		System.out.println(a);


		if (1 == 1) {

			int b = 20; //局部变量超出作用范围之后，会立即回收

			System.out.println(b);
		}

		//System.out.println(b);


		if (1 == 1) {
			short b = 30;

			System.out.println(a);
		}


	}
}
