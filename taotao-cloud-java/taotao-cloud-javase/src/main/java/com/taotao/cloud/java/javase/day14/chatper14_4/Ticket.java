package com.taotao.cloud.java.javase.day14.chatper14_4;
/**
 * 票类
 * @author shuigedeng
 *
 */
public class Ticket implements Runnable{
	
	private int ticket=100;
	//创建锁
	//private Object obj=new Object();
	
	@Override
	public void run() {
		
		while(true) {
			synchronized (this) {//this ---当前对象
				if(ticket<=0) {
					break;
				}
				System.out.println(Thread.currentThread().getName()+"卖了第"+ticket+"票");
				ticket--;
			}
			
		}
	}
	
}
