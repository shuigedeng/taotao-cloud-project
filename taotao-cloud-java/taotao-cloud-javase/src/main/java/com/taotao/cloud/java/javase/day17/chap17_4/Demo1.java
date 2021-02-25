package com.taotao.cloud.java.javase.day17.chap17_4;

public class Demo1 {
	public static void main(String[] args) {
		// 枚举和switch语句的使用
		Season season = Season.WINTER;
		switch (season) { // byte short int char String 枚举
		case SPRING:
			System.out.println("春天");
			break;
		case SUMMER:
			System.out.println("夏天");
			break;
		case AUTUMN:
			System.out.println("秋天");
			break;
		case WINTER:
			System.out.println("冬天");
			break;
		default:
			break;
		}
	}
}
