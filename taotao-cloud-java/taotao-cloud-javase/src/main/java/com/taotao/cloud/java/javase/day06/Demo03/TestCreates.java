package com.taotao.cloud.java.javase.day06.Demo03;

public class TestCreates{

	public static void main(String[] args){

		//先声明、再分配空间
		int[] array1;

		array1 = new int[4];

		//System.out.println( array1[0] );

		//声明并分配空间
		int[] array2 = new int[4];


		//声明并赋值（繁）
		int[] array3;
		array3 = new int[]{ 11 , 22 , 33};

		for(int i = 0 ; i < array3.length ; i++){
			System.out.println( array3[i] );
		}

		//声明并赋值（简）
		int[] array4 = { 66,77,88,99 };//不支持换行书写

		for(int i = 0 ; i < array4.length ; i++){
			System.out.println( array4[i] );
		}
	}
}
