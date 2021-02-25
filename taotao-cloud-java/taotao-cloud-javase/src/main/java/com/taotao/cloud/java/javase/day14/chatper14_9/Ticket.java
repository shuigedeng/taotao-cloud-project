package com.taotao.cloud.java.javase.day14.chatper14_9;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ticket implements Runnable{
	private int ticket=100;
	private Lock lock=new ReentrantLock();
	@Override
	public void run() {
		while(true) {
			lock.lock();
			try {
				if(ticket<=0) {
					break;
				}
				System.out.println(Thread.currentThread().getName()+"卖了第"+ticket+"张票");
				ticket--;
			} finally {
				lock.unlock();
			}
			
		}
	}
	
}
