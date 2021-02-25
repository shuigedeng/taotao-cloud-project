package com.taotao.cloud.java.javase.day04.Demo07;

import java.util.Scanner;

public class TestNestedFor2{

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);

		for(int k = 1 ; k <= 3 ; k++){

			System.out.println("---第"+k+"个班---");

			//求一个班，5位同学的平均成绩
			double sum = 0.0;

			for(int i = 1 ; i <= 5 ; i++){

				System.out.println("请输入第" + i +"位同学的成绩：");

				double score = input.nextDouble();

				sum += score;

			}

			double avg = sum / 5;

			System.out.println("第" + k + "班5位同学的平均分：" + avg);

		}

	}
}
