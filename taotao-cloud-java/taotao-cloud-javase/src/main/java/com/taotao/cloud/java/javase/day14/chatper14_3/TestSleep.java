package com.taotao.cloud.java.javase.day14.chatper14_3;

public class TestSleep {
	public static void main(String[] args) {
		SleepThread s1=new SleepThread();
		s1.start();
		SleepThread s2=new SleepThread();
		s2.start();
	}
}
