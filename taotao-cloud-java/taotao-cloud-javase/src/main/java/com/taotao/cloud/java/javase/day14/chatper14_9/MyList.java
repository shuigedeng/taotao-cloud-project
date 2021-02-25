package com.taotao.cloud.java.javase.day14.chatper14_9;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的使用
 * @author wgy
 *
 */
public class MyList {
	//创建锁
	private Lock lock=new ReentrantLock();
	private String[] str= {"A","B","","",""};
	private int count=2;
	
	public void add(String value) {
		lock.lock();
		try {
			str[count]=value;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			System.out.println(Thread.currentThread().getName()+"添加了"+value);
		} finally {
			lock.unlock();
		}
		
		
	}
	
	public String[] getStr() {
		return str;
	}
	
}
