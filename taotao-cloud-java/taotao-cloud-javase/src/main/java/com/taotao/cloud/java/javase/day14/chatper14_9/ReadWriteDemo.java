package com.taotao.cloud.java.javase.day14.chatper14_9;
/**
 * 演示读写锁的使用
 * ReentrantReadWriteLock
 * @author wgy
 *
 */

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReadWriteDemo {
	//创建读写锁
	private ReentrantReadWriteLock rrl=new ReentrantReadWriteLock();
	//获取读锁
	private ReadLock readLock=rrl.readLock();
	//获取写锁
	private WriteLock writeLock=rrl.writeLock();
	
	//互斥锁
	private ReentrantLock lock=new ReentrantLock();
	
	private String value;
	
	//读取
	public String getValue() {
		//使用读锁上锁
		lock.lock();
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("读取:"+this.value);
			return this.value;
		}finally {
			lock.unlock();
		}
	}
	//写入
	public void setValue(String value) {
		lock.lock();
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("写入:"+value);
			this.value=value;
		}finally {
			lock.unlock();
		}
	}
	
}
