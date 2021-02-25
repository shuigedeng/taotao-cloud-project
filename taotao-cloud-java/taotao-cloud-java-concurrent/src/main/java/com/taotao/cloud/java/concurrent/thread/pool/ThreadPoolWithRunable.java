package com.taotao.cloud.java.thread.pool;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class ThreadPoolWithRunable {


    /**
     * 通过线程池执行线程
     *
     * @param args
     */
    public static void main(String[] args) {
        //创建一个线程池
        ExecutorService pool = newCachedThreadPool();
        for (int i = 1; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("thread name: " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();
    }

}
