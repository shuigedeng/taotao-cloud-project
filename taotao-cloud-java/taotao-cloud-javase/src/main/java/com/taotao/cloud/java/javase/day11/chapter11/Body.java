package com.taotao.cloud.java.javase.day11.chapter11;
//身体类
public class Body {
	private String name;
	
	//头类
	class Header{
		public void show() {
			System.out.println(name);
		}
	}
}
