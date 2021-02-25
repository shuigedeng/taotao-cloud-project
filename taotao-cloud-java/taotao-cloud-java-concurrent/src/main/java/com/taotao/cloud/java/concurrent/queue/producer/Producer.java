package com.taotao.cloud.java.queue.producer;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {

            System.out.println("I have made a product:" + Thread.currentThread().getName());
            String temp = "A Product, 生产线程：" + Thread.currentThread().getName();
            queue.put(temp);    //如果队列是满的话，会阻塞当前线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}  

