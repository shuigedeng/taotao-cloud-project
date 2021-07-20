package com.taotao.cloud.java.javase.day14.chatper14_4;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 线程安全问题:
 * 两个线程同时向一个数组中存放数据
 * @author shuigedeng
 *
 */
public class ThreadSafe {
	private static int index=0;
	public static void main(String[] args)  throws Exception{
		//创建数组
		String[] s=new String[5];
		//创建两个操作
		Runnable runnableA=new Runnable() {
			
			@Override
			public void run() {
				//同步代码块
				synchronized (s) {
					s[index]="hello";
					index++;
				}
				
			}
		};
		Runnable runnableB=new Runnable() {
			
			@Override
			public void run() {
				synchronized (s) {
					s[index]="world";
					index++;
				}
				
			}
		};
		
		//创建两个线程对象
		Thread a=new Thread(runnableA,"A");
		Thread b=new Thread(runnableB,"B");
		a.start();
		b.start();
		
		a.join();//加入线程
		b.join();//加入线程
		
		System.out.println(Arrays.toString(s));
		
	}
}
