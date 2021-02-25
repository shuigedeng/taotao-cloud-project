package com.taotao.cloud.java.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁，可以实现读写分离锁定，读操作并发进行，写操作锁定单个线程
 * <p>
 * 如果有一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁。
 * 如果有一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁。
 *
 * @author
 */
public class MyReentrantReadWriteLock {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final MyReentrantReadWriteLock test = new MyReentrantReadWriteLock();

        new Thread() {
            @Override
            public void run() {
                test.get(Thread.currentThread());
                test.write(Thread.currentThread());
            }

            ;
        }.start();

        new Thread() {
            @Override
            public void run() {
                test.get(Thread.currentThread());
                test.write(Thread.currentThread());
            }

            ;
        }.start();

    }

    /**
     * 读操作,用读锁来锁定
     *
     * @param thread
     */
    public void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作完毕");
        } finally {
            rwl.readLock().unlock();
        }
    }

    /**
     * 写操作，用写锁来锁定
     *
     * @param thread
     */
    public void write(Thread thread) {
        rwl.writeLock().lock();
        ;
        try {
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行写操作");
            }
            System.out.println(thread.getName() + "写操作完毕");
        } finally {
            rwl.writeLock().unlock();
        }
    }
}
