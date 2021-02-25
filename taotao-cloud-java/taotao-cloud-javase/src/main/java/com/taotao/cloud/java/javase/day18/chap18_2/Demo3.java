package com.taotao.cloud.java.javase.day18.chap18_2;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;

/**
 * Instant；时间戳
 * ZoneId:时区
 * @author wgy
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		//1创建Instant：时间戳
		Instant instant=Instant.now();
		System.out.println(instant.toString());
		System.out.println(instant.toEpochMilli());
		System.out.println(System.currentTimeMillis());
		//2添加减少时间
		
		Instant instant2 = instant.plusSeconds(10);
		
		System.out.println(Duration.between(instant, instant2).toMillis());
		
		//3ZoneId
		Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
		for (String string : availableZoneIds) {
			System.out.println(string);
		}
		
		System.out.println(ZoneId.systemDefault().toString());
		
		//1 Date --->Instant---->LocalDateTime
		System.out.println("-------------Date --->Instant---->LocalDateTime-----------");
		Date date=new Date();
		Instant instant3 = date.toInstant();
		System.out.println(instant3);
		
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant3, ZoneId.systemDefault());
		System.out.println(localDateTime);
		
		//1 LocalDateTime --->Instant---->Date
		System.out.println("-------------LocalDateTime --->Instant---->Date-----------");
	
		Instant instant4 = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		System.out.println(instant4);
		Date from = Date.from(instant4);
		System.out.println(from);
		
	}
}
