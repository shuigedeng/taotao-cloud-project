package com.taotao.cloud.java.javase.day18.chap18_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeFormatter类的使用
 * @author shuigedeng
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		//创建DateTimeFormatter
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		//(1)把时间格式化成字符串
		String format = dtf.format(LocalDateTime.now());
		System.out.println(format);
		//(2)把字符串解析成时间
		LocalDateTime localDateTime = LocalDateTime.parse("2020/03/10 10:20:35", dtf);
		System.out.println(localDateTime);
	}
}
