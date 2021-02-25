package com.taotao.cloud.java.javase.day11.chapter11_6;

import java.util.Date;

import javax.xml.crypto.Data;

public class Demo1 {
	public static void main(String[] args) {
		//1创建Date对象
		//今天
		Date date1=new Date();
		System.out.println(date1.toString());
		System.out.println(date1.toLocaleString());
		//昨天
		Date date2=new Date(date1.getTime()-(60*60*24*1000));
		System.out.println(date2.toLocaleString());
		//2方法after before
		boolean b1=date1.after(date2);
		System.out.println(b1);
		boolean b2=date1.before(date2);
		System.out.println(b2);
		
		//比较 compareTo();
		int d=date2.compareTo(date1);
		System.out.println(d);
		//比较是否相等 equals()
		boolean b3=date1.equals(date2);
		System.out.println(b3);
		
		
	}
}
