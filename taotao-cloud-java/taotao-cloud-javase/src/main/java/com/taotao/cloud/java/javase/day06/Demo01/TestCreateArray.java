package com.taotao.cloud.java.javase.day06.Demo01;

public class TestCreateArray{

	public static void main(String[] args){

		int[] a = new int[8]; //在内存中创建长度为5的整数数组

		a[0] = 11;
		a[1] = 22;
		a[2] = 33;
		a[3] = 44;
		a[4] = 55;
		a[5] = 66;
		a[6] = 77;
		a[7] = 88;
		
		/*
		System.out.println( a[0] );
		System.out.println( a[1] );
		System.out.println( a[2] );
		System.out.println( a[3] );
		System.out.println( a[4] );
		*/

		//					i < 8
		for(int i = 0 ; i < a.length ; i++){// 1 <= 5    0 <= 4     0 < 5
			System.out.println( a[i] );
		}

	}
}
