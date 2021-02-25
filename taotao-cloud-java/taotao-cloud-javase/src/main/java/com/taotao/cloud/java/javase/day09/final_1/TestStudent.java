package com.taotao.cloud.java.javase.day09.final_1;

import java.util.Arrays;

public class TestStudent {
	public static void main(String[] args) {
		//final修饰基本类型
		final int num=20;
		//num=30;
		final int[] nums=new int[] {10,20,30};
		//nums=new int[5];
		nums[0]=100;
		System.out.println(Arrays.toString(nums));
		final Student s=new Student();
//		s=new Student();
		s.name="aaa";
		s.name="bbb";
		System.out.println(s.name);
		
	}
}
