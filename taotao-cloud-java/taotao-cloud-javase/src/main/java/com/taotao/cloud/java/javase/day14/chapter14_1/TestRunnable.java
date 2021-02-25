package com.taotao.cloud.java.javase.day14.chapter14_1;

public class TestRunnable {
	public static void main(String[] args) {
//		//1创建MyRunnable对象,表示线程要执行的功能
//		MyRunnable runnable=new MyRunnable();
//		//2创建线程对象
//		Thread thread=new Thread(runnable, "我的线程1");
//		//3启动
//		thread.start();
//		
//		for(int i=0;i<50;i++) {
//			System.out.println("main............"+i);
//		}
		
		//创建可运行对象
		Runnable runnable=new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<100;i++) {
					System.out.println(Thread.currentThread().getName()+" ........."+i);
				}
			}
		};
		//创建线程对象
		Thread thread=new Thread(runnable, "我的线程1");
		//启动线程
		thread.start();
		
		for(int i=0;i<50;i++) {
			System.out.println("main............"+i);
		}
	}
}
