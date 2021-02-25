package com.taotao.cloud.java.javase.day14.chatper14_5;

public class Girl extends Thread {
	@Override
	public void run() {
		synchronized (MyLock.b) {
			System.out.println("女孩拿到了b");
			synchronized (MyLock.a) {
				System.out.println("女孩拿到了a");
				System.out.println("女孩可以吃东西了...");
			}
		}
	}
}
