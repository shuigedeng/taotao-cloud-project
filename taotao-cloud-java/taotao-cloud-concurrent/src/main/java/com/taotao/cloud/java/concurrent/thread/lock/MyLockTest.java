package com.taotao.cloud.java.concurrent.thread.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLockTest {

	private static ArrayList<Integer> arrayList = new ArrayList<Integer>();
	static Lock lock = new ReentrantLock(); // 注意这个地方

	public static <E> void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				Thread thread = Thread.currentThread();

				lock.lock();
				try {
					System.out.println(thread.getName() + "得到了锁");
					for (int i = 0; i < 5; i++) {
						arrayList.add(i);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					lock.unlock();
					System.out.println(thread.getName() + "释放了锁");
				}

			}

			;
		}.start();

		new Thread() {
			@Override
			public void run() {
				Thread thread = Thread.currentThread();
				lock.lock();
				try {
					System.out.println(thread.getName() + "得到了锁");
					for (int i = 0; i < 5; i++) {
						arrayList.add(i);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					lock.unlock();
					System.out.println(thread.getName() + "释放了锁");
				}

			}

			;
		}.start();
	}

}
