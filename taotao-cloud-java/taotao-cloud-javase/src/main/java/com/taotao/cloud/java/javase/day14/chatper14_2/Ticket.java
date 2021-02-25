package com.taotao.cloud.java.javase.day14.chatper14_2;
/**
 * 票类(共享资源)
 * @author wgy
 *
 */
public class Ticket implements Runnable {
	private int ticket=100;//100张票

	
	@Override
	public void run() {
		while(true) {
			if(ticket<=0) {
				break;
			}
			System.out.println(Thread.currentThread().getName()+" 卖了第"+ticket+"张票");
			ticket--;
		}
		
	}
}
