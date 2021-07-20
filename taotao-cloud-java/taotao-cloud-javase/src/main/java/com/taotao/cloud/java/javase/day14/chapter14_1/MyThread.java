package com.taotao.cloud.java.javase.day14.chapter14_1;
/**
 * 线程类
 * @author shuigedeng
 *
 */
public class MyThread extends Thread {
	
	public MyThread() {
		// TODO Auto-generated constructor stub
	}
	public MyThread(String name) {
		super(name);
	}
	 
	
	@Override
	public void run() {
		for(int i=0;i<100;i++) {
			//this.getId获取线程Id 
			//this.getName获取线程名称
			//第一种方式 this.getId和this.getName(); 
			//System.out.println("线程id:"+this.getId()+" 线程名称:"+this.getName()+" 子线程............."+i);
			//第二种方式 Thread.currentThread() 获取当前线程
			System.out.println("线程id:"+Thread.currentThread().getId()+" 线程名称:"+Thread.currentThread().getName()+" 子线程。。。。。。。"+i);
		}
	}
}
