package com.taotao.cloud.java.javase.day14.chatper14_3;

public class TestJoin {
	public static void main(String[] args) {
		JoinThread j1=new JoinThread();
		j1.start();
		
		try {
			j1.join();//加入当前线程(main)，并阻塞当前线程,直到加入线程执行完毕
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//for 主线程
		for(int i=0;i<20;i++) {
			System.out.println(Thread.currentThread().getName()+"========="+i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
