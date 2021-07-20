package com.taotao.cloud.java.javase.day14.chatper14_10;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue的使用
 * @author shuigedeng
 *
 */
public class Demo5 {
	public static void main(String[] args) throws Exception {
		//1创建安全队列
		ConcurrentLinkedQueue<Integer> queue=new ConcurrentLinkedQueue<>();
		//2入队操作
		Thread t1=new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=1;i<=5;i++) {
					queue.offer(i);
				}
			}
		});
		Thread t2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=6;i<=10;i++) {
					queue.offer(i);
				}
			}
		});
		//3启动线程
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		System.out.println("-------------出队-------------");
		//4出队操作
		int size=queue.size();
		for(int i=0;i<size;i++) {
			System.out.println(queue.poll());
		}
		
	}
}
