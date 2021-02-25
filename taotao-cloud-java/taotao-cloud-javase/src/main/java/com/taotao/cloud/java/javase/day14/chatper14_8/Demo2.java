package com.taotao.cloud.java.javase.day14.chatper14_8;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 演示Callable接口的使用
 * Callable和Runnable接口的区别
 * (1)Callable接口中call方法有返回值,Runnable接口中run方法没有返回值
 * (2)Callable接口中call方法有声明异常，Runnable接口中run方法没有异常
 * @author wgy
 *
 */
public class Demo2 {
	public static void main(String[] args) throws Exception{
		//功能需求：使用Callable实现1-100和
		//1创建Callable对象
		Callable<Integer> callable=new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println(Thread.currentThread().getName()+"开始计算");
				int sum=0;
				for(int i=1;i<=100;i++) {
					sum+=i;
					Thread.sleep(100);
				}
				return sum;
			}
		};
		//2把Callable对象 转成可执行任务
		FutureTask<Integer> task=new FutureTask<>(callable);
		
		//3创建线程
		Thread thread=new Thread(task);
		
		//4启动线程
		thread.start();
		
		//5获取结果(等待call执行完毕，才会返回)
		Integer sum=task.get();
		System.out.println("结果是:"+sum);
	}
}
