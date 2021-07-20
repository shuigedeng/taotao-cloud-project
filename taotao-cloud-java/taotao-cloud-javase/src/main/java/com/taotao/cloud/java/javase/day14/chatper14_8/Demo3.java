package com.taotao.cloud.java.javase.day14.chatper14_8;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用线程池计算1-100的和
 * @author shuigedeng
 *
 */
public class Demo3 {
	public static void main(String[] args) throws Exception{
		//1创建线程池
		ExecutorService es=Executors.newFixedThreadPool(1);
		//2提交任务 Future:表示将要执行完任务的结果
		Future<Integer> future=es.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				System.out.println(Thread.currentThread().getName()+"开始计算");
				int sum=0;
				for(int i=1;i<=100;i++) {
					sum+=i;
					Thread.sleep(10);
				}
				return sum;
			}
		});
		
		//3获取任务结果,等待任务执行完毕才会返回.

		System.out.println(future.get());
		
		//4关闭线程池
		es.shutdown();
		
		
		
	}
}
