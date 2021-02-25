package com.taotao.cloud.java.javase.day14.chatper14_8;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

/**
 * 使用两个线程，并发计算1~50、51~100的和，再进行汇总统计。
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) throws Exception{
		//1创建线程池
		ExecutorService es=Executors.newFixedThreadPool(2);
		//2提交任务
		Future<Integer> future1=es.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				int sum=0;
				for(int i=1;i<=50;i++) {
					sum+=i;
				}
				System.out.println("1-50计算完毕");
				return sum;
			}
		});
		Future<Integer> future2=es.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				int sum=0;
				for(int i=51;i<=100;i++) {
					sum+=i;
				}
				System.out.println("51-100计算完毕");
				return sum;
			}
		});
		//3获取结果
		int sum=future1.get()+future2.get();
		System.out.println("结果是:"+sum);
		//4关闭线程池
		es.shutdown();
		
		
	}
}
