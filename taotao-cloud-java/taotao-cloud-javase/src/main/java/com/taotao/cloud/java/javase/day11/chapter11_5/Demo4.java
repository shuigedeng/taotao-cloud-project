package com.taotao.cloud.java.javase.day11.chapter11_5;
/**
 * 已知String str = "this is a text";
1.将str中的单词单独获取出来
2.将str中的text替换为practice
3.在text前面插入一个easy
4.将每个单词的首字母改为大写

 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		String str="this is a text";
		//1将str中的单词单独获取出来
		String[] arr=str.split(" ");
		System.out.println("-----1将str中的单词单独获取出来-------");
		for (String s : arr) {
			System.out.println(s);
		}
		//2.将str中的text替换为practice
		System.out.println("-----2.将str中的text替换为practice-------");
		String str2=str.replace("text", "practice");
		System.out.println(str2);
		//3在text前面插入一个easy
		System.out.println("-----3在text前面插入一个easy-------");
		String str3=str.replace("text", "easy text");
		System.out.println(str3);
		//4将每个单词的首字母改为大写
		for(int i=0;i<arr.length;i++) {
			char first=arr[i].charAt(0);
			//把第一个字符转成大写
			char upperfirst=Character.toUpperCase(first);
			String news=upperfirst+arr[i].substring(1);
			System.out.println(news);
		}
	}
}
