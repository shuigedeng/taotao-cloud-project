package com.taotao.cloud.java.javase.day04.Demo07;

public class TestNestedFor4{

	public static void main(String[] args){


		for(int i = 1 ; i <= 9; i++){

			for(int j = 1 ; j <= i ; j++){
				System.out.print( j + " * " + i +" = " + (j*i) +"\t" );
			}
			System.out.println();

		}




	}
}
