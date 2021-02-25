package com.taotao.cloud.java.javase.day14.chatper14_5;

public class TestDeadLock {
	public static void main(String[] args) {
		Boy boy=new Boy();
		Girl girl=new Girl();
		girl.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boy.start();
	}
}
