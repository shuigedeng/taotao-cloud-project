package com.taotao.cloud.java.javase.day14.chatper14_5;

public class Boy extends Thread{
	@Override
	public void run() {
		synchronized (MyLock.a) {
			System.out.println("男孩拿到了a");
			synchronized (MyLock.b) {
				System.out.println("男孩拿到了b");
				System.out.println("男孩可以吃东西了...");
			}
		}
	}
}
