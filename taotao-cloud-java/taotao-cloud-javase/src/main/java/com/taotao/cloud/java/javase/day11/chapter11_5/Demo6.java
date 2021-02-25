package com.taotao.cloud.java.javase.day11.chapter11_5;

import java.math.BigDecimal;

/**
 * 验证StringBuilder效率高于String
 * @author wgy
 *
 */
public class Demo6 {
	public static void main(String[] args) {
		//开始时间
		long start=System.currentTimeMillis();
		String string="";
		for(int i=0;i<99999;i++) {
			string+=i;
		}
		System.out.println(string);
//		StringBuilder sb=new StringBuilder();
//		for(int i=0;i<99999;i++) {
//			sb.append(i);
//		}
//		System.out.println(sb.toString());
		long end=System.currentTimeMillis();
		System.out.println("用时:"+(end-start));
		
		BigDecimal b1=new BigDecimal("10");
		BigDecimal b2=new BigDecimal("3");
		System.out.println(b1.divide(b2));
		
	}
}
