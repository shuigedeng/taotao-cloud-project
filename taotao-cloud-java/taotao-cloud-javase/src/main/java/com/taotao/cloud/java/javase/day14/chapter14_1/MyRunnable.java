package com.taotao.cloud.java.javase.day14.chapter14_1;

public class MyRunnable implements Runnable{

	@Override
	public void run() {
		for(int i=0;i<100;i++) {
			System.out.println(Thread.currentThread().getName()+" ........."+i);
		}
	}

}
