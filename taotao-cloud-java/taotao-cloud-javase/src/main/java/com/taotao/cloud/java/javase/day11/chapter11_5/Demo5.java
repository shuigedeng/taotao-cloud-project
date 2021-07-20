package com.taotao.cloud.java.javase.day11.chapter11_5;
/**
 * StringBuffer和StringBuilder的使用
 * 和String区别(1)效率比String高 (2)比String节省内存 
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) {
		//StringBuffer sb=new StringBuffer();
		StringBuilder sb=new StringBuilder();
		//1 append();追加
		sb.append("java世界第一");
		System.out.println(sb.toString());
		sb.append("java真香");
		System.out.println(sb.toString());
		sb.append("java不错");
		System.out.println(sb.toString());
		//2 insert();添加
		sb.insert(0, "我在最前面");
		System.out.println(sb.toString());
		//3 replace();替换
		sb.replace(0, 5, "hello");
		System.out.println(sb.toString());
		//4 delete();删除
		sb.delete(0, 5);
		System.out.println(sb.toString());
		//清空
		sb.delete(0, sb.length());
		System.out.println(sb.length());
		
		
	}
}
