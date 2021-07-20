package com.taotao.cloud.java.javase.day14.chatper14_10;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 案例2：使用阻塞队列实现生产者和消费者
 * @author shuigedeng
 *
 */
public class Demo7 {
	public static void main(String[] args) {
		//1创建队列
		ArrayBlockingQueue<Integer> queue=new ArrayBlockingQueue<>(6);
		//2创建两个线程
		Thread t1=new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<30;i++) {
					try {
						queue.put(i);
						System.out.println(Thread.currentThread().getName()+"生产了第"+i+"号面包");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}, "晨晨");
		
		Thread t2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<30;i++) {
					try {
						Integer num=queue.take();
						System.out.println(Thread.currentThread().getName()+"消费了第"+i+"号面包");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}, "冰冰");
		
		//启动线程
		t1.start();
		t2.start();
	
		
	}
}
