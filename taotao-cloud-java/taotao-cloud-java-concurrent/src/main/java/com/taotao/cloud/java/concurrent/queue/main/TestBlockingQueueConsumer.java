package com.taotao.cloud.java.queue.main;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class TestBlockingQueueConsumer implements Runnable {
    BlockingQueue<String> queue;
    Random random = new Random();

    public TestBlockingQueueConsumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(10));
            System.out.println(Thread.currentThread().getName() + "trying...");
            String temp = queue.take();//如果队列为空，会阻塞当前线程  
            System.out.println(Thread.currentThread().getName() + " get a job " + temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
