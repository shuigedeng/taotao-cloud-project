package com.taotao.cloud.java.javase.day14.chatper14_3;

public class YieldThread extends Thread {
	@Override
	public void run() {
		for(int i=0;i<10;i++) {
			System.out.println(Thread.currentThread().getName()+"........"+i);
			//主动放弃cpu
			Thread.yield();
		}
	}
}
