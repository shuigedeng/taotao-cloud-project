package com.taotao.cloud.java.javase.day04.Demo06;

import java.util.Scanner;

public class TestContinue{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		//控制台录入5位同学成绩，如果有任何一位同学的成绩产生非法数据（不满足0~100之间的数字）时，结束本次的统计，进入下一次循环当中

		double sum = 0.0;

		for(int i = 1; i <= 5; ){
			System.out.println("请输入第" + i + "位同学的成绩：");

			double score = input.nextDouble();

			if(score < 0 || score >100.0){
				//非法数据
				continue;
			}

			sum = sum + score;

			i++;
		}

		double avg = sum / 5;

		System.out.println("平均分：" + avg);
	}
}
