package com.taotao.cloud.java.javase.day18.chap18_2;

import java.time.LocalDateTime;

/**
 * LocalDateTime的使用
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		//1创建本地时间
		LocalDateTime localDateTime=LocalDateTime.now();
		//LocalDateTime localDateTime2=LocalDateTime.of(year, month, dayOfMonth, hour, minute)
		System.out.println(localDateTime);
		System.out.println(localDateTime.getYear());
		System.out.println(localDateTime.getMonthValue());
		System.out.println(localDateTime.getDayOfMonth());
		
		//2添加两天
		LocalDateTime localDateTime2 = localDateTime.plusDays(2);
		System.out.println(localDateTime2);
		
		//3减少一个月
		LocalDateTime localDateTime3 = localDateTime.minusMonths(1);
		System.out.println(localDateTime3);
	}
}
