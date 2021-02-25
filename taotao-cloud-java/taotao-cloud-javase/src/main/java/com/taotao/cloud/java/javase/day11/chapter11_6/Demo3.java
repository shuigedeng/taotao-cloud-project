package com.taotao.cloud.java.javase.day11.chapter11_6;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo3 {
	public static void main(String[] args) throws Exception{
		//1创建SimpleDateFormat对象  y 年 M 月
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH-mm-ss");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		//2创建Date
		Date date=new Date();
		//格式化date(把日期转成字符串)
		String str=sdf.format(date);
		System.out.println(str);
		//解析 （把字符串转成日期）
		Date date2=sdf.parse("1990/05/01");
		System.out.println(date2);
	}
}
