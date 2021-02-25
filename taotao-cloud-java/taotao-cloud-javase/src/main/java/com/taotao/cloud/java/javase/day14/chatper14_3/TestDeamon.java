package com.taotao.cloud.java.javase.day14.chatper14_3;

public class TestDeamon {
	public static void main(String[] args) {
		//创建线程(默认前台线程)
		DeamonThread d1=new DeamonThread();
		//设置线程为守护线程
		d1.setDaemon(true);
		d1.start();
		
		for(int i=0;i<10;i++) {
			System.out.println("主线程:----------"+i);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
