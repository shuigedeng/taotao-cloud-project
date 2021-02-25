package com.taotao.cloud.java.javase.day14.chapter14_1;

public class TestThread {
	public static void main(String[] args) {
		//1创建线程对象
		MyThread myThread=new MyThread("我的子线程1");
		//2启动线程,不能使用run方法
		//修改线程名称
		//myThread.setName("我的子线程1");
		
		myThread.start();//myThread.run()
		
		//创建第二个线程对象
		MyThread myThread2=new MyThread("我的子线程2");
		
		//myThread2.setName("我的子线程2");
		myThread2.start();

		//主线程执行
		for(int i=0;i<50;i++) {
			System.out.println("主线程======================"+i);
		}
	}
}
