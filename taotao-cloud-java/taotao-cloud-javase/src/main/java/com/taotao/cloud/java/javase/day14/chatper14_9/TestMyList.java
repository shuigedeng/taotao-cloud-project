package com.taotao.cloud.java.javase.day14.chatper14_9;

import java.util.Arrays;

public class TestMyList {
	public static void main(String[] args) throws Exception {
		MyList list=new MyList();
		Runnable runnable=new Runnable() {
			
			@Override
			public void run() {
				list.add("hello");
			}
		};
		Runnable runnable2=new Runnable() {
			
			@Override
			public void run() {
				list.add("world");
			}
		};
		
		Thread t1=new Thread(runnable);
		Thread t2=new Thread(runnable2);
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		System.out.println(Arrays.toString(list.getStr()));
		
			
	}
}
