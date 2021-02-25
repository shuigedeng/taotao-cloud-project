package com.taotao.cloud.java.futures;

import static java.util.concurrent.Executors.newCachedThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 有时候使用Future感觉很丑陋，因为你需要间隔检查Future是否已完成，而使用回调会直接收到返回通知。
 *
 * @author wilson
 */
public class FutureExample {

	public static void main(String[] args) throws Exception {
		//创建线程池
		ExecutorService executor = newCachedThreadPool();
		//创建线程
		Runnable task1 = new Runnable() {
			@Override
			public void run() {
				//do something
				System.out.println("第一个runnable线程.....");
			}
		};
		//创建线程
		Callable<Integer> task2 = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				//do something
				System.out.println("第二个Callable线程……");
				return new Integer(100);
			}
		};
		//提交第一个线程
		Future<?> f1 = executor.submit(task1);
		//提交第二个线程
		Future<Integer> f2 = executor.submit(task2);
		//打印第1个线程是否结束
		System.out.println("第1个线程是否结束? " + f1.isDone());
		//打印第2个线程是否结束
		System.out.println("第2个线程是否结束? " + f2.isDone());
		//等待第一个线程结束
		while (f1.isDone()) {
			System.out.println("第一个线程结束");
			break;
		}
		//等待第二个线程结束
		while (f2.isDone()) {
			System.out.println("第二个线程结束，返回结果: " + f2.get());
			break;
		}
	}

}
