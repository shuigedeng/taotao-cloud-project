package com.taotao.cloud.java.javase.day06.Demo04;

public class TestGetAvg{
	
	public static void main(String[] args){
		
		int[] numbers = new int[]{55,66,77,88,99};
		
		int sum = 0;
		
		for(int i = 0 ; i < numbers.length ; i++){
			
			sum += numbers[i];
			
		}
		
		double avg = sum / numbers.length;
		
		System.out.println(avg);
		
	}
}
