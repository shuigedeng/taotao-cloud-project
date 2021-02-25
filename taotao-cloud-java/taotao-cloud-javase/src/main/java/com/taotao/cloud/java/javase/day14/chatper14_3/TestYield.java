package com.taotao.cloud.java.javase.day14.chatper14_3;

public class TestYield {
	public static void main(String[] args) {
		YieldThread y1=new YieldThread();
		YieldThread y2=new YieldThread();
		
		y1.start();
		y2.start();
	}
}
