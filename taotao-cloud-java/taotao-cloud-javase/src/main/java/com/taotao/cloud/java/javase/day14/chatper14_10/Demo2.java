package com.taotao.cloud.java.javase.day14.chatper14_10;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用多线程操作CopyOnWriteArrayList
 * @author shuigedeng
 *
 */
public class Demo2 {
	public static void main(String[] args) {
		//1创建集合
		CopyOnWriteArrayList<String> list=new CopyOnWriteArrayList<>();
		//2使用多线程操作
		ExecutorService es=Executors.newFixedThreadPool(5);
		//3提交任务
		for(int i=0;i<5;i++) {
			es.submit(new Runnable() {
				
				@Override
				public void run() {
					for(int j=0;j<10;j++) {
						list.add(Thread.currentThread().getName()+"...."+new Random().nextInt(1000));
					}
				}
			});
		}
		//4关闭线程池
		es.shutdown();
		while(!es.isTerminated()) {}
		//5打印结果
		System.out.println("元素个数:"+list.size());
		for (String string : list) {
			System.out.println(string);
		}
	}
}
