package com.taotao.cloud.java.javase.day14.chatper14_5;
/**
 * 票类
 * @author shuigedeng
 *
 */
public class Ticket implements Runnable{
	
	private static int ticket=100;
	//创建锁
	//private Object obj=new Object();
	
	@Override
	public void run() {
		
		while(true) {
			if(!sale()) {
				break;
			}
		}
	}
	//卖票(同步方法)
	public   static boolean sale() {//锁 this  静态方法 锁 Ticket.class
		synchronized (Ticket.class) {
			if(ticket<=0) {
				return false;
			}
			System.out.println(Thread.currentThread().getName()+"卖了第"+ticket+"票");
			ticket--;
			return true;
		}
			
		
	}
	
}
