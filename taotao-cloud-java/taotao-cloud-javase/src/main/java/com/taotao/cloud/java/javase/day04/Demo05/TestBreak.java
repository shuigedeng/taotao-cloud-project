package com.taotao.cloud.java.javase.day04.Demo05;

import java.util.Scanner;

public class TestBreak{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		//控制台录入5位同学成绩，如果有任何一位同学的成绩产生非法数据（不满足0~100之间的数字）时，将直接退出整合循环操作

		double sum = 0.0;

		boolean flag = true;//合法

		for(int i = 1; i <= 5; i++){

			System.out.println("请输入第" + i + "位同学的成绩：");

			double score = input.nextDouble();

			if(score < 0 || score > 100.0){
				flag = false;//非法数据
				break;
			}

			sum = sum + score;

		}

		if( flag == true ){ //根据flag标记决定是否需要计算和输出平均分
			double avg = sum / 5;

			System.out.println("平均分：" + avg);
		}else{
			System.out.println("非法数据，请重新运行程序计算平均分");
		}



	}
}
