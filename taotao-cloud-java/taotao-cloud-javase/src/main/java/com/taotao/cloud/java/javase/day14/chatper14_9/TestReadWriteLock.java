package com.taotao.cloud.java.javase.day14.chatper14_9;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestReadWriteLock {
	public static void main(String[] args) {
		ReadWriteDemo readWriteDemo=new ReadWriteDemo();
		//创建线程池
		ExecutorService es=Executors.newFixedThreadPool(20);
		
		Runnable read=new Runnable() {
			
			@Override
			public void run() {
				readWriteDemo.getValue();
			}
		};
		Runnable write=new Runnable() {
			
			@Override
			public void run() {
				readWriteDemo.setValue("张三:"+new Random().nextInt(100));
			}
		};
		long start=System.currentTimeMillis();
		//分配2个写的任务
		for(int i=0;i<2;i++) {
			es.submit(write);
		}

		//分配18读取任务
		for(int i=0;i<18;i++) {
			es.submit(read);
		}
			
		es.shutdown();//关闭
		while(!es.isTerminated()) {//空转
			
		}
		long end=System.currentTimeMillis();
		System.out.println("用时:"+(end-start));
		
		
	}
}
