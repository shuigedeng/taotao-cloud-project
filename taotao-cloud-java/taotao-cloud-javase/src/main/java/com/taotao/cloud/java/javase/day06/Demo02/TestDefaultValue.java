package com.taotao.cloud.java.javase.day06.Demo02;

public class TestDefaultValue{
	
	public static void main(String[] args){
		
		int[] a = new int[5];
		
		for(int i = 0 ; i < a.length ; i++){
			System.out.println( a[i] );
		}
		
		double[] b = new double[5];
		
		for(int i = 0 ; i < b.length ; i++){
			System.out.println( b[i] );
		}
		
		String[] strs = new String[4];
		
		for(int i = 0 ; i < strs.length ; i++){
			System.out.println( strs[i] );
		}
		
	}
}
