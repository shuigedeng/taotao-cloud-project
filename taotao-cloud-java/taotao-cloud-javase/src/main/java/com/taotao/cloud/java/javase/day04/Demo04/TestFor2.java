package com.taotao.cloud.java.javase.day04.Demo04;

import java.util.Scanner;

public class TestFor2{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		//计算5位同学的平均分


		double sum = 0.0;//总和

		for(int i = 1; i <= 5; i++){

			//1.循环控制台录入分数
			System.out.println("请输入第" + i + "位同学的成绩：");

			double score = input.nextDouble();

			//2.累加总和
			sum = sum + score;

		}

		double avg = sum / 5;

		System.out.println("平均分：" + avg);

	}
}
