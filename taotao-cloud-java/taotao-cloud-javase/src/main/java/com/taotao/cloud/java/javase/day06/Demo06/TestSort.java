package com.taotao.cloud.java.javase.day06.Demo06;

import java.util.Arrays;

public class TestSort{

	public static void main(String[] args){

		int[] nums = new int[]{4,3,5,2,1};

		//借助JDK提供的数组工具，进行排序
		Arrays.sort(nums);

		//第一次遍历（升序）
		for(int i = 0 ; i < nums.length ; i++){
			System.out.println(nums[i]);
		}


		//降序：需要手工的方式完成元素的倒置  5 2 3 4 1

		for(int i = 0 ; i < nums.length / 2 ; i++){// i = 0

			int temp = nums[i];// int temp = 1;

			nums[i] = nums[ nums.length - 1 - i];

			nums[ nums.length - 1 - i] = temp;

		}

		//第二次遍历（降序）
		for(int i = 0 ; i < nums.length ; i++){
			System.out.println(nums[i]);
		}



		//两值交换，借助第三变量
		/*
		int a = 10;
		int b = 20;
		int c = a;//将a中的值保存在c中
		a = b;//将b中的值保存在a中
		b = c;//将c中的值保存在b中
		*/
	}
}
